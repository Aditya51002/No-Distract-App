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
}
