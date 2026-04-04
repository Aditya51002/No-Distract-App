package com.example.focusapp.enforcement

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.focusapp.MainActivity
import com.example.focusapp.ui.screens.BlockOverlayScreen
import com.example.focusapp.ui.theme.FocusAppTheme
import com.example.focusapp.viewmodel.FocusTheme

class BlockOverlayActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val blockedPackage = intent.getStringExtra(EXTRA_BLOCKED_PACKAGE).orEmpty()
        val remainingSeconds = intent.getLongExtra(EXTRA_REMAINING_SECONDS, 0L).coerceAtLeast(0L)

        setContent {
            FocusAppTheme(focusTheme = FocusTheme.DEEP_WORK) {
                BlockOverlayScreen(
                    blockedPackage = blockedPackage,
                    remainingSeconds = remainingSeconds,
                    onBackToFocus = {
                        startActivity(
                            Intent(this, MainActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            }
                        )
                        finish()
                    }
                )
            }
        }
    }

    companion object {
        const val EXTRA_BLOCKED_PACKAGE = "blocked_package"
        const val EXTRA_REMAINING_SECONDS = "remaining_seconds"
    }
}

