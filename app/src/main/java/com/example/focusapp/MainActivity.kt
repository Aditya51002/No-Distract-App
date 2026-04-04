package com.example.focusapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.focusapp.enforcement.UsageStatsMonitorService
import com.example.focusapp.navigation.Screen
import com.example.focusapp.ui.screens.*
import com.example.focusapp.ui.theme.*
import com.example.focusapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, UsageStatsMonitorService::class.java))
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = viewModel()
            val focusTheme by viewModel.currentTheme.collectAsState()
            
            FocusAppTheme(focusTheme = focusTheme) {
                MainContent(viewModel)
            }
        }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarScreens = listOf(
        BottomNavItem("Home", Screen.Home.route, Icons.Default.Home),
        BottomNavItem("Stats", Screen.Stats.route, Icons.Default.ShowChart),
        BottomNavItem("Insights", Screen.Insights.route, Icons.Default.Lightbulb),
        BottomNavItem("Settings", Screen.Settings.route, Icons.Default.Settings)
    )

    val showBottomBar = currentDestination?.route in bottomBarScreens.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = DarkBackground,
                    contentColor = TextPrimary,
                    tonalElevation = 8.dp
                ) {
                    bottomBarScreens.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = TextSecondary,
                                unselectedTextColor = TextSecondary,
                                indicatorColor = GlassWhite
                            )
                        )
                    }
                }
            }
        },
        containerColor = DarkBackground
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel, 
                    onStartFocus = {
                        navController.navigate(Screen.FocusSetup.route)
                    },
                    onNavigateToChallenges = {
                        navController.navigate(Screen.Challenges.route)
                    }
                )
            }
            composable(Screen.Stats.route) {
                StatsScreen(
                    viewModel = viewModel,
                    onNavigateToComparison = {
                        navController.navigate(Screen.Comparison.route)
                    }
                )
            }
            composable(Screen.Insights.route) {
                InsightsScreen(viewModel = viewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = viewModel)
            }
            composable(Screen.FocusSetup.route) {
                FocusSetupScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onSelectApps = { navController.navigate(Screen.SelectApps.route) },
                    onStartSession = { navController.navigate(Screen.ActiveSession.route) }
                )
            }
            composable(Screen.SelectApps.route) {
                SelectAppsScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onSave = { navController.popBackStack() }
                )
            }
            composable(Screen.ActiveSession.route) {
                ActiveFocusSessionScreen(
                    viewModel = viewModel,
                    onFinish = {
                        viewModel.completeSession()
                        navController.navigate(Screen.SessionComplete.route) {
                            popUpTo(Screen.Home.route)
                        }
                    },
                    onStop = { navController.popBackStack() }
                )
            }
            composable(Screen.SessionComplete.route) {
                SessionCompleteScreen(
                    viewModel = viewModel,
                    onGoHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onStartAgain = {
                        navController.navigate(Screen.FocusSetup.route) {
                            popUpTo(Screen.Home.route)
                        }
                    }
                )
            }
            composable(Screen.BlockOverlay.route) {
                BlockOverlayScreen(onBackToFocus = {
                    navController.popBackStack()
                })
            }
            composable(Screen.Challenges.route) {
                ChallengesScreen(viewModel = viewModel, onBack = { navController.popBackStack() })
            }
            composable(Screen.Comparison.route) {
                ComparisonScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
