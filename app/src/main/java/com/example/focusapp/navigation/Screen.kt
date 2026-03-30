package com.example.focusapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object SelectApps : Screen("select_apps")
    object FocusSetup : Screen("focus_setup")
    object ActiveSession : Screen("active_session")
    object BlockOverlay : Screen("block_overlay")
    object Stats : Screen("stats")
    object SessionComplete : Screen("session_complete")
    object Settings : Screen("settings")
    
    // NEW ROUTES
    object Insights : Screen("insights")
    object Comparison : Screen("comparison")
    object Challenges : Screen("challenges")
}
