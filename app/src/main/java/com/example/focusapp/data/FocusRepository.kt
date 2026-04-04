package com.example.focusapp.data

import com.example.focusapp.data.local.BlockedAppDao
import com.example.focusapp.data.local.BlockedAppEntity
import com.example.focusapp.data.local.FocusPreferencesDataStore
import com.example.focusapp.data.local.ReminderDao
import com.example.focusapp.data.local.ReminderEntity
import com.example.focusapp.data.network.BlockedAppDto
import com.example.focusapp.data.network.CompleteSessionRequest
import com.example.focusapp.data.network.FocusLockApi
import com.example.focusapp.data.network.ReminderDto
import com.example.focusapp.data.network.SessionConfigDto
import com.example.focusapp.data.network.StartSessionRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class FocusRepository(
    private val api: FocusLockApi,
    private val blockedAppDao: BlockedAppDao,
    private val reminderDao: ReminderDao,
    private val prefs: FocusPreferencesDataStore
) {
    fun observeBlockedApps(): Flow<List<BlockedAppEntity>> = blockedAppDao.observeAll()
    fun observeReminders(): Flow<List<ReminderEntity>> = reminderDao.observeAll()

    val strictModeFlow = prefs.strictModeFlow
    val notificationsEnabledFlow = prefs.notificationsEnabledFlow
    val selectedDurationMinutesFlow = prefs.selectedDurationMinutesFlow
    val selectedThemeFlow = prefs.selectedThemeFlow
    val activeSessionIdFlow = prefs.activeSessionIdFlow
    val sessionActiveFlow = prefs.sessionActiveFlow
    val sessionEndEpochMillisFlow = prefs.sessionEndEpochMillisFlow

    suspend fun setAvailableApps(apps: List<Pair<String, String>>) {
        val selected = blockedAppDao.getSelectedPackages().toSet()
        val entities = apps.map { (name, packageName) ->
            BlockedAppEntity(
                packageName = packageName,
                appName = name,
                isSelected = packageName in selected
            )
        }
        blockedAppDao.upsertAll(entities)
    }

    suspend fun toggleAppSelection(packageName: String) {
        val current = blockedAppDao.isSelected(packageName)
        blockedAppDao.setSelected(packageName = packageName, isSelected = !current)
    }

    suspend fun saveStrictMode(enabled: Boolean) = prefs.setStrictMode(enabled)
    suspend fun saveNotificationsEnabled(enabled: Boolean) = prefs.setNotificationsEnabled(enabled)
    suspend fun saveSelectedDuration(minutes: Int) = prefs.setSelectedDurationMinutes(minutes)
    suspend fun saveTheme(theme: String) = prefs.setTheme(theme)

    suspend fun syncBlockedAppsWithBackend() {
        runCatching {
            val remoteApps = api.getBlockedApps().data
            if (remoteApps.isNotEmpty()) {
                val selectedPackages = blockedAppDao.getSelectedPackages().toSet()
                val entities = remoteApps.map { dto ->
                    BlockedAppEntity(
                        packageName = dto.packageName,
                        appName = dto.appName,
                        isSelected = dto.packageName in selectedPackages,
                        remoteId = dto.id
                    )
                }
                blockedAppDao.upsertAll(entities)
            }
        }

        val local = blockedAppDao.getAll().filter { it.isSelected }
        local.forEach { app ->
            if (app.remoteId.isNullOrBlank()) {
                runCatching {
                    val created = api.addBlockedApp(
                        BlockedAppDto(
                            appName = app.appName,
                            packageName = app.packageName,
                            category = "User Selected"
                        )
                    ).data
                    blockedAppDao.updateRemoteId(app.packageName, created.id)
                }
            }
        }
    }

    suspend fun refreshReminders() {
        runCatching {
            val remote = api.getReminders().data
            val mapped = remote.map {
                ReminderEntity(
                    id = it.id ?: "${it.title}_${it.reminderTime}",
                    label = it.title,
                    time = it.reminderTime,
                    remoteId = it.id
                )
            }
            reminderDao.clear()
            reminderDao.upsertAll(mapped)
        }
    }

    suspend fun addReminder(label: String, time: String) {
        val localId = System.currentTimeMillis().toString()
        val local = ReminderEntity(id = localId, label = label.trim(), time = time.trim())
        reminderDao.upsert(local)

        runCatching {
            val created = api.createReminder(
                ReminderDto(
                    title = label.trim(),
                    reminderTime = time.trim()
                )
            ).data
            reminderDao.upsert(local.copy(id = created.id ?: localId, remoteId = created.id))
        }
    }

    suspend fun deleteReminder(reminderId: String) {
        reminderDao.deleteById(reminderId)
        // Backend delete reminder route is not implemented in current API.
    }

    suspend fun fetchRemoteSessionConfig() {
        runCatching {
            val cfg = api.getSessionConfig().data ?: return@runCatching
            prefs.setSelectedDurationMinutes(cfg.duration)
            prefs.setStrictMode(cfg.strictMode)
            prefs.setNotificationsEnabled(!cfg.notificationsMuted)
            prefs.setTheme(cfg.theme)
        }
    }

    suspend fun pushSessionConfig() {
        val config = SessionConfigDto(
            duration = selectedDurationMinutesFlow.firstValue(25),
            strictMode = strictModeFlow.firstValue(false),
            notificationsMuted = !notificationsEnabledFlow.firstValue(true),
            theme = selectedThemeFlow.firstValue("DEEP_WORK")
        )
        runCatching { api.updateSessionConfig(config) }
    }

    suspend fun startSession(
        durationMinutes: Int,
        blockedAppsCount: Int,
        title: String = "Focus Session"
    ): String? {
        var sessionId: String? = null
        runCatching {
            val started = api.startSession(
                StartSessionRequest(
                    sessionTitle = title,
                    duration = durationMinutes,
                    blockedAppsCount = blockedAppsCount
                )
            ).data
            sessionId = started.id
        }

        val end = System.currentTimeMillis() + (durationMinutes * 60 * 1000L)
        prefs.setActiveSession(sessionId = sessionId, isActive = true, endEpochMillis = end)
        return sessionId
    }

    suspend fun completeActiveSession(distractionAttempts: Int) {
        val sessionId = activeSessionIdFlow.firstValue(null)
        if (!sessionId.isNullOrBlank()) {
            runCatching {
                api.completeSession(sessionId, CompleteSessionRequest(distractionAttempts = distractionAttempts))
            }
        }
        prefs.clearActiveSession()
    }

    suspend fun isSessionActiveNow(): Boolean = prefs.isSessionActiveNow()

    suspend fun isPackageBlocked(packageName: String): Boolean {
        return blockedAppDao.isSelected(packageName)
    }
}

private suspend fun <T> Flow<T>.firstValue(default: T): T {
    return kotlin.runCatching { first() }.getOrDefault(default)
}
