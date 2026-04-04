package com.example.focusapp.data

import android.content.Context
import com.example.focusapp.data.local.FocusDatabase
import com.example.focusapp.data.local.FocusPreferencesDataStore
import com.example.focusapp.data.network.NetworkClient

class AppContainer(context: Context) {
    private val database = FocusDatabase.getInstance(context)
    private val prefs = FocusPreferencesDataStore(context)

    val authRepository = AuthRepository(
        authApi = NetworkClient.authApi,
        prefs = prefs
    )

    val focusRepository = FocusRepository(
        api = NetworkClient.focusLockApi,
        blockedAppDao = database.blockedAppDao(),
        reminderDao = database.reminderDao(),
        prefs = prefs
    )
}

