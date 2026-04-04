package com.example.focusapp.enforcement

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.example.focusapp.FocusApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class BlockAccessibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val packageName = event.packageName?.toString().orEmpty()
        if (packageName.isBlank() || packageName == this.packageName) return

        val focusRepository = FocusApp.instance.appContainer.focusRepository
        val shouldBlock = runBlocking {
            focusRepository.isSessionActiveNow() && focusRepository.isPackageBlocked(packageName)
        }

        if (shouldBlock) {
            val sessionEnd = runBlocking { focusRepository.sessionEndEpochMillisFlow.firstValue(0L) }
            val remaining = ((sessionEnd - System.currentTimeMillis()) / 1000L).coerceAtLeast(0L)
            OverlayLauncher.launch(this, packageName, remaining)
        }
    }

    override fun onInterrupt() = Unit
}

private suspend fun <T> kotlinx.coroutines.flow.Flow<T>.firstValue(default: T): T {
    return kotlin.runCatching { first() }.getOrDefault(default)
}
