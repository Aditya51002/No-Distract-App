package com.example.focusapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focusapp.ui.components.ChallengeCard
import com.example.focusapp.ui.components.SectionHeader
import com.example.focusapp.ui.theme.DarkBackground
import com.example.focusapp.ui.theme.TextPrimary
import com.example.focusapp.ui.theme.TextSecondary
import com.example.focusapp.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengesScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val challenges by viewModel.challenges.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Challenges", fontWeight = FontWeight.Bold) },
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
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Complete challenges to earn XP and level up!",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionHeader("Active Challenges")
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(challenges) { challenge ->
                    ChallengeCard(
                        title = challenge.title,
                        progress = challenge.progress,
                        goal = challenge.goal,
                        isCompleted = challenge.isCompleted
                    )
                }
            }
        }
    }
}
