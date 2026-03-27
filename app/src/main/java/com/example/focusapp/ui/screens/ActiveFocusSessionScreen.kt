package com.example.focusapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focusapp.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ActiveFocusSessionScreen(
    onFinish: () -> Unit,
    onStop: () -> Unit
) {
    var timeLeft by remember { mutableStateOf(1500) } // 25 mins in seconds
    var isActive by remember { mutableStateOf(true) }

    LaunchedEffect(isActive) {
        while (isActive && timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0) {
            onFinish()
        }
    }

    val progress = timeLeft / 1500f
    val infiniteTransition = rememberInfiniteTransition()
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Stay Focused",
            style = MaterialTheme.typography.headlineSmall,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(48.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(300.dp)
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(PrimaryNeon.copy(alpha = glowAlpha), Color.Transparent),
                                center = center,
                                radius = size.width / 1.5f
                            )
                        )
                    }
            )

            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(280.dp),
                color = PrimaryNeon,
                strokeWidth = 8.dp,
                trackColor = GlassWhite,
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = formatTime(timeLeft),
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "REMAINING",
                    style = MaterialTheme.typography.labelLarge,
                    color = TextSecondary,
                    letterSpacing = 2.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "“Your focus determines your reality.”",
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Light,
            modifier = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledIconButton(
                onClick = { isActive = !isActive },
                modifier = Modifier.size(64.dp),
                colors = IconButtonDefaults.filledIconButtonColors(containerColor = GlassWhite)
            ) {
                Icon(
                    imageVector = if (isActive) Icons.Default.Pause else Icons.Default.Stop, // Simple toggle for UI
                    contentDescription = "Pause",
                    tint = TextPrimary
                )
            }

            Button(
                onClick = onStop,
                modifier = Modifier
                    .height(64.dp)
                    .width(160.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentPink)
            ) {
                Text("End Session", fontWeight = FontWeight.Bold)
            }
        }
    }
}

fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return "%02d:%02d".format(mins, secs)
}
