package com.example.focusapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focusapp.ui.components.CustomButton
import com.example.focusapp.ui.components.GlassCard
import com.example.focusapp.ui.components.SectionHeader
import com.example.focusapp.ui.theme.*
import com.example.focusapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusSetupScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onSelectApps: () -> Unit,
    onStartSession: () -> Unit
) {
    var selectedDuration by remember { mutableStateOf(25) }
    var strictMode by remember { mutableStateOf(false) }
    var notifications by remember { mutableStateOf(true) }
    val selectedApps by viewModel.apps.collectAsState()
    val selectedAppsCount = selectedApps.count { it.isSelected }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setup Session", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                )
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(24.dp)
        ) {
            SectionHeader("Duration")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(25, 45, 60, 90).forEach { duration ->
                    DurationChip(
                        duration = duration,
                        isSelected = selectedDuration == duration,
                        onClick = { selectedDuration = duration },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader("Configuration")
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                ToggleOption(
                    label = "Strict Mode",
                    description = "Cannot exit session early",
                    checked = strictMode,
                    onCheckedChange = { strictMode = it }
                )
                Divider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
                ToggleOption(
                    label = "Block Notifications",
                    description = "Silence all alerts",
                    checked = notifications,
                    onCheckedChange = { notifications = it }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionHeader("Selected Apps")
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectApps() }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$selectedAppsCount Apps Selected",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "Change",
                        style = MaterialTheme.typography.bodyMedium,
                        color = PrimaryNeon,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = "Start Session ($selectedDuration min)",
                onClick = {
                    // TODO: Connect system service here
                    // TODO: Add real background service for timer
                    onStartSession()
                }
            )
        }
    }
}

@Composable
fun DurationChip(
    duration: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) PrimaryNeon else GlassWhite)
            .border(1.dp, if (isSelected) PrimaryNeon else GlassBorder, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${duration}m",
            color = if (isSelected) Color.White else TextPrimary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ToggleOption(
    label: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryNeon,
                uncheckedThumbColor = TextSecondary,
                uncheckedTrackColor = GlassBorder
            )
        )
    }
}
