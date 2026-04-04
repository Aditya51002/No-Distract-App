package com.example.focusapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focusapp.ui.components.CustomButton
import com.example.focusapp.ui.theme.*

@Composable
fun BlockOverlayScreen(
    blockedPackage: String = "",
    remainingSeconds: Long = 0L,
    onBackToFocus: () -> Unit
) {
    val mins = (remainingSeconds / 60).coerceAtLeast(0)
    val secs = (remainingSeconds % 60).coerceAtLeast(0)

    // TODO: Trigger this screen when blocked app is opened
    // This would typically be a separate Activity or a system overlay
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = AccentPink,
                modifier = Modifier.size(80.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "🚫 App Blocked",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Focus session in progress.\nGet back to work!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = TextSecondary
            )

            if (blockedPackage.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = blockedPackage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
            
            Text(
                text = "%02d:%02d remaining".format(mins, secs),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = PrimaryNeon
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            CustomButton(
                text = "Back to Focus",
                onClick = onBackToFocus,
                containerColor = PrimaryNeon
            )
        }
    }
}
