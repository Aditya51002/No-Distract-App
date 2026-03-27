package com.example.focusapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focusapp.ui.components.CustomButton
import com.example.focusapp.ui.components.SectionHeader
import com.example.focusapp.ui.components.StatCard
import com.example.focusapp.ui.theme.SecondaryNeon
import com.example.focusapp.ui.theme.TextPrimary
import com.example.focusapp.ui.theme.TextSecondary
import com.example.focusapp.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onStartFocus: () -> Unit
) {
    val focusTime by viewModel.focusTimeToday.collectAsState()
    val streak by viewModel.currentStreak.collectAsState()
    val sessions by viewModel.sessionsToday.collectAsState()
    val blocked by viewModel.distractionsBlocked.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Stay focused, Aditya 👋",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Ready to conquer your goals?",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(
            text = "Start Focus Session",
            onClick = onStartFocus
        )

        SectionHeader("Quick Stats")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                label = "Focus Time",
                value = focusTime,
                icon = Icons.Default.HourglassTop,
                color = SecondaryNeon,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Streak",
                value = "$streak days",
                icon = Icons.Default.LocalFireDepartment,
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                label = "Sessions",
                value = sessions.toString(),
                icon = Icons.Default.Block,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Blocked",
                value = blocked.toString(),
                icon = Icons.Default.Block,
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            )
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}
