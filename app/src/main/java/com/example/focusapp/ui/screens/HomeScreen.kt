package com.example.focusapp.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focusapp.ui.components.*
import com.example.focusapp.ui.theme.*
import com.example.focusapp.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onStartFocus: () -> Unit,
    onNavigateToChallenges: () -> Unit
) {
    val focusTime by viewModel.focusTimeToday.collectAsState()
    val streak by viewModel.currentStreak.collectAsState()
    val sessions by viewModel.sessionsToday.collectAsState()
    val blocked by viewModel.distractionsBlocked.collectAsState()
    
    // NEW FEATURES STATE
    val score by viewModel.focusScore.collectAsState()
    val xp by viewModel.xp.collectAsState()
    val level by viewModel.level.collectAsState()
    val resisted by viewModel.distractionsResisted.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Stay focused, Aditya 👋",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "You resisted $resisted distractions today 💪",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        XPProgressBar(level = level, xp = xp, maxXP = 500)

        Spacer(modifier = Modifier.height(24.dp))

        FocusScoreCard(score = score)

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(
            text = "Start Focus Session",
            onClick = onStartFocus
        )

        SectionHeader("Focus activity")
        HeatmapGrid()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionHeader("Challenges")
            Text(
                text = "View All",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onNavigateToChallenges() }
            )
        }
        
        val challenges by viewModel.challenges.collectAsState()
        challenges.take(1).forEach { challenge ->
            ChallengeCard(
                title = challenge.title,
                progress = challenge.progress,
                goal = challenge.goal,
                isCompleted = challenge.isCompleted
            )
        }

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
                icon = Icons.Default.CheckCircle,
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
