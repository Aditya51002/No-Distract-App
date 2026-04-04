package com.example.focusapp.data.network

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

data class AuthPayload(
    val user: UserDto,
    val accessToken: String
)

data class UserDto(
    @SerializedName("_id") val id: String,
    val name: String,
    val email: String
)

data class BlockedAppDto(
    @SerializedName("_id") val id: String? = null,
    val appName: String,
    val packageName: String,
    val category: String = "Uncategorized",
    val isBlocked: Boolean = true,
    val icon: String? = null
)

data class ReminderDto(
    @SerializedName("_id") val id: String? = null,
    val title: String,
    val reminderTime: String,
    val enabled: Boolean = true,
    val repeatType: String = "DAILY"
)

data class SessionConfigDto(
    val duration: Int = 25,
    val strictMode: Boolean = false,
    val notificationsMuted: Boolean = true,
    val theme: String = "DEEP_WORK"
)

data class StartSessionRequest(
    val sessionTitle: String,
    val mode: String = "Deep Work",
    val duration: Int,
    val blockedAppsCount: Int
)

data class CompleteSessionRequest(
    val distractionAttempts: Int
)

data class FocusSessionDto(
    @SerializedName("_id") val id: String,
    val duration: Int,
    val completed: Boolean,
    val distractionAttempts: Int = 0
)

