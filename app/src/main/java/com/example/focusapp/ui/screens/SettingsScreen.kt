package com.example.focusapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focusapp.ui.components.GlassCard
import com.example.focusapp.ui.components.SectionHeader
import com.example.focusapp.ui.theme.*

@Composable
fun SettingsScreen() {
    var darkMode by remember { mutableStateOf(true) }
    var notifications by remember { mutableStateOf(true) }
    var strictMode by remember { mutableStateOf(false) }

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
            text = "Customize your experience",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        SectionHeader("Preferences")
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            ToggleOption(
                label = "Dark Mode",
                description = "Enable dark theme",
                checked = darkMode,
                onCheckedChange = { darkMode = it }
            )
            Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            ToggleOption(
                label = "Push Notifications",
                description = "Get alerts and reminders",
                checked = notifications,
                onCheckedChange = { notifications = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader("Defaults")
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            ToggleOption(
                label = "Always Strict Mode",
                description = "Default to strict focus sessions",
                checked = strictMode,
                onCheckedChange = { strictMode = it }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionHeader("About")
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            SettingsItem(label = "Version", value = "1.0.0")
            Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
            SettingsItem(label = "Developer", value = "Aditya")
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun SettingsItem(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, color = TextSecondary, fontWeight = FontWeight.Bold)
    }
}
