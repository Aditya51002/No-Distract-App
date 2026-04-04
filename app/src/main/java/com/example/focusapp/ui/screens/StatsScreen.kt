package com.example.focusapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focusapp.ui.components.GlassCard
import com.example.focusapp.ui.components.SectionHeader
import com.example.focusapp.ui.theme.*
import com.example.focusapp.viewmodel.MainViewModel

@Composable
fun StatsScreen(
    viewModel: MainViewModel,
    onNavigateToComparison: () -> Unit
) {
    val weeklyFocus by viewModel.weeklyFocusMinutes.collectAsState()
    val blocked by viewModel.distractionsBlocked.collectAsState()
    val completionRate by viewModel.completionRate.collectAsState()

    val maxFocus = (weeklyFocus.maxOrNull() ?: 1).coerceAtLeast(1)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Analytics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Track your productivity",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        SectionHeader("Focus Time")
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyFocus.forEach { minutes ->
                    val barHeight = ((minutes.toFloat() / maxFocus) * 120f).coerceAtLeast(24f).dp
                    Bar(height = barHeight)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToComparison() }
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.TrendingUp, contentDescription = null, tint = PrimaryNeon)
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "View Comparison", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "Compare with previous periods", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = TextSecondary)
            }
        }

        SectionHeader("Summary")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoCard(
                label = "Distractions",
                value = blocked.toString(),
                color = AccentPink,
                modifier = Modifier.weight(1f)
            )
            InfoCard(
                label = "Completion",
                value = completionRate,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun Bar(height: androidx.compose.ui.unit.Dp) {
    Box(
        modifier = Modifier
            .width(20.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(GlassWhite),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .width(20.dp)
                .height(height)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(Brush.verticalGradient(listOf(SecondaryNeon, PrimaryNeon)))
        )
    }
}

@Composable
fun InfoCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}
