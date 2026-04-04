package com.example.focusapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.focusapp.ui.components.InsightCard
import com.example.focusapp.ui.components.SectionHeader
import com.example.focusapp.ui.theme.TextPrimary
import com.example.focusapp.ui.theme.TextSecondary
import com.example.focusapp.viewmodel.MainViewModel

@Composable
fun InsightsScreen(viewModel: MainViewModel) {
    val insights by viewModel.insights.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Smart Insights",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "AI-powered focus analysis",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(32.dp))

        SectionHeader("Personalized for you")
        
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(insights) { insight ->
                InsightCard(
                    title = insight.title,
                    description = insight.description,
                    iconName = insight.icon
                )
            }
        }
    }
}
