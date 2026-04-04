package com.example.focusapp

import android.app.Application
import com.example.focusapp.data.AppContainer

class FocusApp : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContainer = AppContainer(this)
    }

    companion object {
        lateinit var instance: FocusApp
            private set
    }
}

