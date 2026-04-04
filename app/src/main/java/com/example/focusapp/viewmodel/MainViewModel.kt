package com.example.focusapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppItem(
    val name: String,
    val packageName: String,
    val iconRes: Int = 0,
    var isSelected: Boolean = false
)

data class FocusTask(
    val id: String,
    val title: String,
    val isCompleted: Boolean = false
)

data class FocusInsight(
    val title: String,
    val description: String,
    val icon: String // Simplified icon name
)

data class Challenge(
    val title: String,
    val progress: Float,
    val goal: String,
    val isCompleted: Boolean = false
)

data class Reminder(
    val id: String,
    val label: String,
    val time: String
)

enum class FocusTheme {
    DEEP_WORK, ZEN, NIGHT, EXAM
}

class MainViewModel : ViewModel() {
    private val _apps = MutableStateFlow(
        listOf(
            AppItem("Instagram", "com.instagram.android"),
            AppItem("YouTube", "com.google.android.youtube"),
            AppItem("Facebook", "com.facebook.katana"),
            AppItem("Twitter", "com.twitter.android"),
            AppItem("Snapchat", "com.snapchat.android"),
            AppItem("Chrome", "com.android.chrome"),
            AppItem("Netflix", "com.netflix.mediaclient"),
            AppItem("TikTok", "com.zhiliaoapp.musically")
        )
    )
    val apps = _apps.asStateFlow()

    private val _focusTasks = MutableStateFlow(
        listOf(
            FocusTask("1", "Complete UI Design"),
            FocusTask("2", "Review Codebase"),
            FocusTask("3", "Write Documentation")
        )
    )
    val focusTasks = _focusTasks.asStateFlow()

    private val _currentTask = MutableStateFlow<FocusTask?>(null)
    val currentTask = _currentTask.asStateFlow()

    // NEW FEATURES STATE
    private val _focusScore = MutableStateFlow(82)
    val focusScore = _focusScore.asStateFlow()

    private val _xp = MutableStateFlow(320)
    val xp = _xp.asStateFlow()

    private val _level = MutableStateFlow("Deep Worker")
    val level = _level.asStateFlow()

    private val _distractionsResisted = MutableStateFlow(12)
    val distractionsResisted = _distractionsResisted.asStateFlow()

    private val _currentTheme = MutableStateFlow(FocusTheme.DEEP_WORK)
    val currentTheme = _currentTheme.asStateFlow()

    private val _insights = MutableStateFlow(
        listOf(
            FocusInsight("Night Owl Focus", "You focus 40% better after 9 PM. 🌙", "Nights"),
            FocusInsight("App Distraction", "Instagram is your biggest distraction source.", "Instagram"),
            FocusInsight("Session Length", "Try 45 min sessions for deeper focus.", "Timer")
        )
    )
    val insights = _insights.asStateFlow()

    private val _challenges = MutableStateFlow(
        listOf(
            Challenge("Daily Focused", 0.66f, "2/3 sessions"),
            Challenge("Distraction Free", 1f, "2 hours", true),
            Challenge("Consistency King", 0.3f, "3 days streak")
        )
    )
    val challenges = _challenges.asStateFlow()

    private val _selectedSessionDurationMinutes = MutableStateFlow(25)
    val selectedSessionDurationMinutes = _selectedSessionDurationMinutes.asStateFlow()

    private val _strictModeEnabled = MutableStateFlow(false)
    val strictModeEnabled = _strictModeEnabled.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled = _notificationsEnabled.asStateFlow()

    private val _activeSessionDurationSeconds = MutableStateFlow(25 * 60)
    val activeSessionDurationSeconds = _activeSessionDurationSeconds.asStateFlow()

    private val _lastCompletedSessionDurationMinutes = MutableStateFlow(25)
    val lastCompletedSessionDurationMinutes = _lastCompletedSessionDurationMinutes.asStateFlow()

    private val _reminders = MutableStateFlow(
        listOf(
            Reminder(id = "morning", label = "Morning Focus", time = "08:00 AM"),
            Reminder(id = "deep-work", label = "Deep Work", time = "02:00 PM")
        )
    )
    val reminders = _reminders.asStateFlow()

    private val _weeklyFocusMinutes = MutableStateFlow(listOf(62, 90, 48, 105, 78, 96, 84))
    val weeklyFocusMinutes = _weeklyFocusMinutes.asStateFlow()

    private val _completionRate = MutableStateFlow("94%")
    val completionRate = _completionRate.asStateFlow()

    // DUMMY STATS
    private val _focusTimeToday = MutableStateFlow("4h 25m")
    val focusTimeToday = _focusTimeToday.asStateFlow()

    private val _currentStreak = MutableStateFlow(5)
    val currentStreak = _currentStreak.asStateFlow()

    private val _sessionsToday = MutableStateFlow(3)
    val sessionsToday = _sessionsToday.asStateFlow()

    private val _distractionsBlocked = MutableStateFlow(124)
    val distractionsBlocked = _distractionsBlocked.asStateFlow()

    fun toggleAppSelection(app: AppItem) {
        _apps.update { list ->
            list.map {
                if (it.packageName == app.packageName) it.copy(isSelected = !it.isSelected) else it
            }
        }
    }

    fun addTask(title: String) {
        if (title.isBlank()) return
        val newTask = FocusTask(id = System.currentTimeMillis().toString(), title = title)
        _focusTasks.update { it + newTask }
    }

    fun toggleTaskCompletion(taskId: String) {
        _focusTasks.update { list ->
            list.map {
                if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
            }
        }
    }

    fun setCurrentTask(task: FocusTask?) {
        _currentTask.value = task
    }

    fun setTheme(theme: FocusTheme) {
        _currentTheme.value = theme
    }

    fun setAvailableApps(installedApps: List<AppItem>) {
        val selectedPackages = _apps.value.filter { it.isSelected }.map { it.packageName }.toSet()
        _apps.value = installedApps.map { app ->
            app.copy(isSelected = app.packageName in selectedPackages)
        }
    }

    fun saveSelectedApps() {
        // Selection is already reflected in state; this hook is kept for backend/local persistence wiring.
    }

    fun setSelectedSessionDuration(minutes: Int) {
        _selectedSessionDurationMinutes.value = minutes
    }

    fun setStrictModeEnabled(enabled: Boolean) {
        _strictModeEnabled.value = enabled
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun startSession() {
        _activeSessionDurationSeconds.value = _selectedSessionDurationMinutes.value * 60
    }

    fun completeSession() {
        val sessionMinutes = _selectedSessionDurationMinutes.value
        _lastCompletedSessionDurationMinutes.value = sessionMinutes
        _sessionsToday.update { it + 1 }
        _focusTimeToday.value = addMinutesToDuration(_focusTimeToday.value, sessionMinutes)
        _xp.update { it + (sessionMinutes / 5) }
    }

    fun addReminder(label: String, time: String) {
        if (label.isBlank() || time.isBlank()) return
        _reminders.update {
            it + Reminder(
                id = System.currentTimeMillis().toString(),
                label = label.trim(),
                time = time.trim()
            )
        }
    }

    fun deleteReminder(reminderId: String) {
        _reminders.update { reminders -> reminders.filterNot { it.id == reminderId } }
    }

    private fun addMinutesToDuration(duration: String, minutesToAdd: Int): String {
        val cleaned = duration.lowercase().replace(" ", "")
        val hours = cleaned.substringBefore("h", "0").toIntOrNull() ?: 0
        val minutes = cleaned.substringAfter("h", "0").substringBefore("m", "0").toIntOrNull() ?: 0
        val totalMinutes = (hours * 60) + minutes + minutesToAdd
        val newHours = totalMinutes / 60
        val newMinutes = totalMinutes % 60
        return "${newHours}h ${newMinutes}m"
    }
}
