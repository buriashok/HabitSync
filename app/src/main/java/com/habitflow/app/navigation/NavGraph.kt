package com.habitflow.newapp.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.habitflow.newapp.ui.components.BottomNavBar
import com.habitflow.newapp.ui.screens.ai.AICoachScreen
import com.habitflow.newapp.ui.screens.analytics.AnalyticsScreen
import com.habitflow.newapp.ui.screens.calendar.CalendarScreen
import com.habitflow.newapp.ui.screens.dashboard.DashboardScreen
import com.habitflow.newapp.ui.screens.habits.HabitsScreen
import com.habitflow.newapp.ui.screens.leaderboard.LeaderboardScreen
import com.habitflow.newapp.ui.screens.login.LoginScreen
import com.habitflow.newapp.ui.screens.profile.ProfileScreen
import com.habitflow.newapp.ui.screens.timer.TimerScreen
import com.habitflow.newapp.ui.theme.AppTheme
import com.habitflow.newapp.viewmodel.HabitViewModel

sealed class Screen(val route: String, val label: String, val icon: String) {
    data object Dashboard : Screen("dashboard", "Dashboard", "dashboard")
    data object Habits : Screen("habits", "Habits", "habits")
    data object Analytics : Screen("analytics", "Analytics", "analytics")
    data object Leaderboard : Screen("leaderboard", "Leaderboard", "leaderboard")
    data object AICoach : Screen("ai", "AI Coach", "ai")
    data object Calendar : Screen("calendar", "Calendar", "calendar")
    data object Timer : Screen("timer", "Timer", "timer")
    data object Profile : Screen("profile", "Profile", "profile")
    data object Login : Screen("login", "Login", "login")
}

val navItems = listOf(
    Screen.Dashboard,
    Screen.Habits,
    Screen.Analytics,
    Screen.Timer,
    Screen.Profile,
)

@Composable
fun HabitFlowNavHost(
    habitViewModel: HabitViewModel = viewModel()
) {
    if (habitViewModel.authLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.bgPrimary),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = AppTheme.colors.textPrimary)
        }
        return
    }
    val startRoute = if (habitViewModel.isLoggedIn) Screen.Dashboard.route else Screen.Login.route

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute != null
            && currentRoute != Screen.Login.route
            && habitViewModel.isLoggedIn

    Scaffold(
        containerColor = AppTheme.colors.bgPrimary,
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute ?: Screen.Dashboard.route,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppTheme.colors.bgPrimary),
            enterTransition = { fadeIn(animationSpec = tween(300)) },
            exitTransition = { fadeOut(animationSpec = tween(200)) },
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    viewModel = habitViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Dashboard.route) {
                DashboardScreen(viewModel = habitViewModel)
            }
            composable(Screen.Habits.route) {
                HabitsScreen(viewModel = habitViewModel)
            }
            composable(Screen.Analytics.route) {
                AnalyticsScreen(viewModel = habitViewModel)
            }
            composable(Screen.Leaderboard.route) {
                LeaderboardScreen()
            }
            composable(Screen.AICoach.route) {
                AICoachScreen()
            }
            composable(Screen.Calendar.route) {
                CalendarScreen(viewModel = habitViewModel)
            }
            composable(Screen.Timer.route) {
                TimerScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    viewModel = habitViewModel,
                    onSignOut = {
                        habitViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Dashboard.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
