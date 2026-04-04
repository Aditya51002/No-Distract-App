package com.example.focusapp.data

import com.example.focusapp.data.local.FocusPreferencesDataStore
import com.example.focusapp.data.network.AuthApi
import com.example.focusapp.data.network.LoginRequest
import com.example.focusapp.data.network.NetworkClient
import com.example.focusapp.data.network.RegisterRequest
import com.example.focusapp.data.network.UserDto
import kotlinx.coroutines.flow.firstOrNull

class AuthRepository(
    private val authApi: AuthApi,
    private val prefs: FocusPreferencesDataStore
) {
    suspend fun login(email: String, password: String): Result<UserDto> = runCatching {
        val envelope = authApi.login(LoginRequest(email = email, password = password))
        val token = envelope.data.accessToken
        prefs.setAuthToken(token)
        NetworkClient.authToken = token
        envelope.data.user
    }

    suspend fun register(name: String, email: String, password: String): Result<UserDto> = runCatching {
        authApi.register(RegisterRequest(name = name, email = email, password = password)).data
    }

    suspend fun me(): Result<UserDto> = runCatching {
        authApi.getMe().data
    }

    suspend fun applyPersistedToken() {
        val token = prefs.authTokenFlow.firstOrNull()
        NetworkClient.authToken = token
    }

    suspend fun logout() {
        prefs.setAuthToken(null)
        NetworkClient.authToken = null
    }
}
