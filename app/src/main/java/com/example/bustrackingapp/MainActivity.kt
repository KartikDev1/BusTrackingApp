package com.example.bustrackingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            val navController = rememberNavController()

            // Display the splash screen first, then navigate to the home screen
            if (showSplash) {
                SplashScreen {
                    showSplash = false
                }
            } else {
                SetupNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home_screen") {
        composable("home_screen") {
            HomeScreen(navController = navController)
        }
        composable("bus_details_screen/{busId}") { backStackEntry ->
            val busId = backStackEntry.arguments?.getString("busId")?.toIntOrNull()
            val bus = getBusById(busId) // Function to fetch bus details
            if (bus != null) {
                BusDetailsScreen(bus = bus, navController = navController)
            }
        }
        composable("saved_routes_screen") {
            SavedRoutesScreen(navController = navController) // Implement this screen
        }
        composable("bus_schedules_screen") {
            BusSchedulesScreen(navController = navController) // Implement this screen
        }
        composable("about_screen") {
            AboutScreen(navController = navController) // Implement this screen
        }
    }
}

fun getBusById(busId: Int?): Bus? {
    // Mock data - replace with actual data retrieval logic
    val busList = listOf(
        Bus(1, "Bus 101", "Route A - B", "On Time"),
        Bus(2, "Bus 102", "Route B - C", "Delayed"),
        Bus(3, "Bus 103", "Route C - D", "On Time"),
        Bus(4, "Bus 201", "Route C - D", "On Time"),
        Bus(5, "Bus 202", "Route C - D", "On Time"),
        Bus(6, "Bus 203", "Route C - D", "On Time")
    )
    return busList.find { it.id == busId }
}
