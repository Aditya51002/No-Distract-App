package com.example.focusapp.enforcement

import android.app.Service
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Intent
import android.os.IBinder
import com.example.focusapp.FocusApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class UsageStatsMonitorService : Service() {
    private val serviceScope = CoroutineScope(Dispatchers.Default)
    private var monitorJob: Job? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (monitorJob?.isActive == true) return START_STICKY

        val focusRepository = FocusApp.instance.appContainer.focusRepository
        val usageStatsManager = getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager

        monitorJob = serviceScope.launch {
            while (isActive) {
                val foregroundPackage = findForegroundPackage(usageStatsManager)
                if (!foregroundPackage.isNullOrBlank()) {
                    val isSessionActive = focusRepository.isSessionActiveNow()
                    val blocked = focusRepository.isPackageBlocked(foregroundPackage)
                    if (isSessionActive && blocked) {
                        val sessionEnd = focusRepository.sessionEndEpochMillisFlow.firstValue(0L)
                        val remaining = ((sessionEnd - System.currentTimeMillis()) / 1000L).coerceAtLeast(0L)
                        OverlayLauncher.launch(this@UsageStatsMonitorService, foregroundPackage, remaining)
                    }
                }
                delay(1200L)
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        monitorJob?.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun findForegroundPackage(usageStatsManager: UsageStatsManager): String? {
        val end = System.currentTimeMillis()
        val start = end - 10_000L
        val events = usageStatsManager.queryEvents(start, end)
        val event = UsageEvents.Event()
        var latest: String? = null

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED) {
                latest = event.packageName
            }
        }
        return latest
    }
}

private suspend fun <T> kotlinx.coroutines.flow.Flow<T>.firstValue(default: T): T {
    return kotlin.runCatching { first() }.getOrDefault(default)
}
