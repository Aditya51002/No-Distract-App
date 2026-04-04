package com.example.focusapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.focusPrefs by preferencesDataStore(name = "focus_prefs")

class FocusPreferencesDataStore(private val context: Context) {
    private object Keys {
        val authToken = stringPreferencesKey("auth_token")
        val strictMode = booleanPreferencesKey("strict_mode")
        val notificationsEnabled = booleanPreferencesKey("notifications_enabled")
        val selectedDurationMinutes = intPreferencesKey("selected_duration_minutes")
        val selectedTheme = stringPreferencesKey("selected_theme")
        val activeSessionId = stringPreferencesKey("active_session_id")
        val sessionActive = booleanPreferencesKey("session_active")
        val sessionEndEpochMillis = longPreferencesKey("session_end_epoch_millis")
    }

    val authTokenFlow: Flow<String?> = context.focusPrefs.data.map { it[Keys.authToken] }
    val strictModeFlow: Flow<Boolean> = context.focusPrefs.data.map { it[Keys.strictMode] ?: false }
    val notificationsEnabledFlow: Flow<Boolean> = context.focusPrefs.data.map { it[Keys.notificationsEnabled] ?: true }
    val selectedDurationMinutesFlow: Flow<Int> = context.focusPrefs.data.map { it[Keys.selectedDurationMinutes] ?: 25 }
    val selectedThemeFlow: Flow<String> = context.focusPrefs.data.map { it[Keys.selectedTheme] ?: "DEEP_WORK" }
    val activeSessionIdFlow: Flow<String?> = context.focusPrefs.data.map { it[Keys.activeSessionId] }
    val sessionActiveFlow: Flow<Boolean> = context.focusPrefs.data.map { it[Keys.sessionActive] ?: false }
    val sessionEndEpochMillisFlow: Flow<Long> = context.focusPrefs.data.map { it[Keys.sessionEndEpochMillis] ?: 0L }

    suspend fun setAuthToken(token: String?) {
        context.focusPrefs.edit { prefs ->
            if (token.isNullOrBlank()) prefs.remove(Keys.authToken) else prefs[Keys.authToken] = token
        }
    }

    suspend fun setStrictMode(enabled: Boolean) = setBoolean(Keys.strictMode, enabled)
    suspend fun setNotificationsEnabled(enabled: Boolean) = setBoolean(Keys.notificationsEnabled, enabled)
    suspend fun setSelectedDurationMinutes(minutes: Int) = setInt(Keys.selectedDurationMinutes, minutes)
    suspend fun setTheme(theme: String) = setString(Keys.selectedTheme, theme)

    suspend fun setActiveSession(sessionId: String?, isActive: Boolean, endEpochMillis: Long) {
        context.focusPrefs.edit { prefs ->
            if (sessionId.isNullOrBlank()) prefs.remove(Keys.activeSessionId) else prefs[Keys.activeSessionId] = sessionId
            prefs[Keys.sessionActive] = isActive
            prefs[Keys.sessionEndEpochMillis] = endEpochMillis
        }
    }

    suspend fun clearActiveSession() {
        context.focusPrefs.edit { prefs ->
            prefs.remove(Keys.activeSessionId)
            prefs[Keys.sessionActive] = false
            prefs[Keys.sessionEndEpochMillis] = 0L
        }
    }

    suspend fun isSessionActiveNow(): Boolean {
        val snapshot = context.focusPrefs.data.first()
        val active = snapshot[Keys.sessionActive] ?: false
        val end = snapshot[Keys.sessionEndEpochMillis] ?: 0L
        return active && (end == 0L || System.currentTimeMillis() < end)
    }

    suspend fun getSessionEndEpochMillis(): Long {
        return context.focusPrefs.data.first()[Keys.sessionEndEpochMillis] ?: 0L
    }

    private suspend fun setBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        context.focusPrefs.edit { prefs -> prefs[key] = value }
    }

    private suspend fun setInt(key: Preferences.Key<Int>, value: Int) {
        context.focusPrefs.edit { prefs -> prefs[key] = value }
    }

    private suspend fun setString(key: Preferences.Key<String>, value: String) {
        context.focusPrefs.edit { prefs -> prefs[key] = value }
    }
}

