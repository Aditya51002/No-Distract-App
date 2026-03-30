package com.example.focusapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.focusapp.ui.components.AnimatedCounterText
import com.example.focusapp.ui.components.CustomButton
import com.example.focusapp.ui.theme.*
import com.example.focusapp.viewmodel.MainViewModel

@Composable
fun SessionCompleteScreen(
    viewModel: MainViewModel,
    onGoHome: () -> Unit,
    onStartAgain: () -> Unit
) {
    val distractions by viewModel.distractionsResisted.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(100.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "🎉 Session Completed!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "You've successfully focused for 25 minutes.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "You resisted ",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
            AnimatedCounterText(
                targetValue = distractions,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = " distractions this session! 💪",
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary
            )
        }
        
        Spacer(modifier = Modifier.height(64.dp))
        
        CustomButton(
            text = "Start Another Session",
            onClick = onStartAgain,
            containerColor = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextButton(
            onClick = onGoHome,
            modifier = Modifier.height(56.dp).fillMaxWidth()
        ) {
            Text("Go to Dashboard", color = TextSecondary, fontWeight = FontWeight.Bold)
        }
    }
}
