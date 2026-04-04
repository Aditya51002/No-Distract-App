package com.example.focusapp.enforcement

import android.content.Context
import android.content.Intent

object OverlayLauncher {
    private var lastLaunchAt = 0L
    private var lastPackage: String? = null

    fun launch(context: Context, blockedPackage: String, remainingSeconds: Long) {
        val now = System.currentTimeMillis()
        val samePackage = blockedPackage == lastPackage
        if (samePackage && now - lastLaunchAt < 2500L) return

        lastLaunchAt = now
        lastPackage = blockedPackage

        context.startActivity(
            Intent(context, BlockOverlayActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                putExtra(BlockOverlayActivity.EXTRA_BLOCKED_PACKAGE, blockedPackage)
                putExtra(BlockOverlayActivity.EXTRA_REMAINING_SECONDS, remainingSeconds)
            }
        )
    }
}

