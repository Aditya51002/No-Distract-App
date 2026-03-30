package com.example.focusapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focusapp.ui.components.GlassCard
import com.example.focusapp.ui.components.SectionHeader
import com.example.focusapp.ui.theme.*
import com.example.focusapp.viewmodel.FocusTheme
import com.example.focusapp.viewmodel.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    var notifications by remember { mutableStateOf(true) }
    var strictMode by remember { mutableStateOf(false) }
    val currentTheme by viewModel.currentTheme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Customize your focus experience",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        SectionHeader("Focus Theme")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FocusTheme.values().forEach { theme ->
                ThemeSelector(
                    theme = theme,
                    isSelected = currentTheme == theme,
                    onClick = { viewModel.setTheme(theme) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        SectionHeader("Smart Reminders")
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            ReminderItem("Morning Focus", "08:00 AM")
            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            ReminderItem("Deep Work", "02:00 PM")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TextButton(
                onClick = { /* TODO: Add reminder logic */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = PrimaryNeon)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add New Reminder", color = PrimaryNeon, fontWeight = FontWeight.Bold)
            }
        }

        SectionHeader("Preferences")
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            ToggleOption(
                label = "Always Strict Mode",
                description = "Default to strict focus sessions",
                checked = strictMode,
                onCheckedChange = { strictMode = it }
            )
            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            ToggleOption(
                label = "Push Notifications",
                description = "Get alerts and reminders",
                checked = notifications,
                onCheckedChange = { notifications = it }
            )
        }

        SectionHeader("About")
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            SettingsItem(label = "Version", value = "1.2.0")
            HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            SettingsItem(label = "Developer", value = "Aditya")
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun ThemeSelector(
    theme: FocusTheme,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (color, name) = when(theme) {
        FocusTheme.DEEP_WORK -> PrimaryNeon to "Deep"
        FocusTheme.ZEN -> Color(0xFF4CAF50) to "Zen"
        FocusTheme.NIGHT -> Color(0xFF9C27B0) to "Night"
        FocusTheme.EXAM -> AccentPink to "Exam"
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color)
                .border(2.dp, if (isSelected) Color.White else Color.Transparent, CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) TextPrimary else TextSecondary,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun ReminderItem(label: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = time, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        IconButton(onClick = { /* TODO: Delete reminder */ }) {
            Icon(Icons.Default.Delete, contentDescription = null, tint = TextSecondary.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun SettingsItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = TextSecondary, fontWeight = FontWeight.Bold)
    }
}
