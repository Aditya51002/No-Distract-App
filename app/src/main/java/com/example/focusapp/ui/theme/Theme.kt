package com.example.focusapp.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.focusapp.viewmodel.FocusTheme

@Composable
fun FocusAppTheme(
    focusTheme: FocusTheme = FocusTheme.DEEP_WORK,
    content: @Composable () -> Unit
) {
    val primaryColor = when (focusTheme) {
        FocusTheme.DEEP_WORK -> PrimaryNeon
        FocusTheme.ZEN -> Color(0xFF4CAF50)
        FocusTheme.NIGHT -> Color(0xFF9C27B0)
        FocusTheme.EXAM -> AccentPink
    }

    val colorScheme = darkColorScheme(
        primary = primaryColor,
        secondary = SecondaryNeon,
        tertiary = AccentPink,
        background = DarkBackground,
        surface = CardBackground,
        onPrimary = TextPrimary,
        onSecondary = TextPrimary,
        onTertiary = TextPrimary,
        onBackground = TextPrimary,
        onSurface = TextPrimary,
    )

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            window.navigationBarColor = DarkBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
