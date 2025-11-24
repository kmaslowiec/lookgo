package com.kmaslowiec.lookgo.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kmaslowiec.lookgo.main.view.MainScreen
import com.kmaslowiec.lookgo.permissions.view.LocationPermissionScreen
import com.kmaslowiec.lookgo.start.view.StartScreen
import com.kmaslowiec.lookgo.welcome.view.WelcomePagerScreen

sealed class Screen(val route: String) {
    data object Start : Screen("start")
    data object Welcome : Screen("welcome")
    data object Main : Screen("main")
    data object LocationPermissionScreen : Screen("locationPermissionScreen")
}

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Start.route,
        modifier = modifier,
    ) {
        composable(Screen.Start.route) {
            StartScreen(
                onNavigateToMain = { navController.navigate(Screen.Main.route) },
                onNavigateToWelcome = { navController.navigate(Screen.Welcome.route) },
                onNavigateToLocationPermission = { navController.navigate(Screen.LocationPermissionScreen.route) },
            )
        }
        composable(Screen.Welcome.route) {
            WelcomePagerScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateToLocationPermission = { navController.navigate(Screen.LocationPermissionScreen.route) },
            )
        }
        composable(Screen.Main.route) {
            MainScreen(
                Modifier.fillMaxSize(),
                onNavigateToLocationPermission = { navController.navigate(Screen.LocationPermissionScreen.route) },
            )
        }
        composable(Screen.LocationPermissionScreen.route) {
            LocationPermissionScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateToMain = { navController.navigate(Screen.Main.route) },
            )
        }
    }
}
