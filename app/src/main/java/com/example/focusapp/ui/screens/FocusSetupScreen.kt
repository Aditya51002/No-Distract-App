package com.example.focusapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import com.example.focusapp.viewmodel.FocusTask
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
    val tasks by viewModel.focusTasks.collectAsState()
    val currentTask by viewModel.currentTask.collectAsState()
    
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Setup Session", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { SectionHeader("Session Duration") }
            item {
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
            }

            item { SectionHeader("Focus Goal (Optional)") }
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentTask?.title ?: "Select a task to focus on",
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (currentTask == null) TextSecondary else TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { showAddTaskDialog = true }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Task", tint = PrimaryNeon)
                        }
                    }
                }
            }

            if (tasks.isNotEmpty()) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        isSelected = currentTask?.id == task.id,
                        onClick = { viewModel.setCurrentTask(if (currentTask?.id == task.id) null else task) },
                        onToggle = { viewModel.toggleTaskCompletion(task.id) }
                    )
                }
            }

            item { SectionHeader("Configuration") }
            item {
                GlassCard(modifier = Modifier.fillMaxWidth()) {
                    ToggleOption(
                        label = "Strict Mode",
                        description = "Cannot exit session early",
                        checked = strictMode,
                        onCheckedChange = { strictMode = it }
                    )
                    HorizontalDivider(color = GlassBorder, modifier = Modifier.padding(vertical = 12.dp))
                    ToggleOption(
                        label = "Block Notifications",
                        description = "Silence all alerts",
                        checked = notifications,
                        onCheckedChange = { notifications = it }
                    )
                }
            }

            item { SectionHeader("Blocked Apps") }
            item {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectApps() }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$selectedAppsCount Apps Blocked",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "Edit",
                            style = MaterialTheme.typography.bodyMedium,
                            color = PrimaryNeon,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                CustomButton(
                    text = "Start Focus Session",
                    onClick = onStartSession
                )
            }
            
            item { Spacer(modifier = Modifier.height(48.dp)) }
        }

        if (showAddTaskDialog) {
            AlertDialog(
                onDismissRequest = { showAddTaskDialog = false },
                title = { Text("New Focus Goal") },
                text = {
                    OutlinedTextField(
                        value = newTaskTitle,
                        onValueChange = { newTaskTitle = it },
                        placeholder = { Text("What are you focusing on?") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryNeon,
                            unfocusedBorderColor = GlassBorder
                        )
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.addTask(newTaskTitle)
                        newTaskTitle = ""
                        showAddTaskDialog = false
                    }) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddTaskDialog = false }) {
                        Text("Cancel")
                    }
                },
                containerColor = CardBackground,
                titleContentColor = TextPrimary,
                textContentColor = TextPrimary
            )
        }
    }
}

@Composable
fun TaskItem(
    task: FocusTask,
    isSelected: Boolean,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) PrimaryNeon.copy(alpha = 0.1f) else GlassWhite)
            .border(1.dp, if (isSelected) PrimaryNeon else Color.Transparent, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onToggle, modifier = Modifier.size(24.dp)) {
            Icon(
                imageVector = if (task.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (task.isCompleted) Color(0xFF4CAF50) else TextSecondary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = task.title,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
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
