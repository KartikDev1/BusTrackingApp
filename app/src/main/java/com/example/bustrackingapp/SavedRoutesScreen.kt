package com.example.bustrackingapp

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SavedRoutesScreen(navController: NavHostController) {
    var selectedRoute by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Routes") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        val savedRoutes = listOf(
            "Route A - B",
            "Route B - C",
            "Route C - D"
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(savedRoutes) { route ->
                    RouteCard(route) { selectedRoute = route }
                }
            }

            if (selectedRoute != null) {
                RouteDetails(selectedRoute!!)
            }
        }
    }
}

@Composable
fun RouteCard(route: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClick),
        elevation = 6.dp,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = Color(0xFF6200EE) // Updated color for better UI
    ) {
        Text(
            text = route,
            fontSize = 18.sp,
            modifier = Modifier.padding(16.dp),
            color = Color.White
        )
    }
}

@Composable
fun RouteDetails(route: String) {
    val stops = when (route) {
        "Route A - B" -> listOf(
            "Mira Road Station",
            "Mira Bhayander Road",
            "Golden Nest",
            "Bhayander Station"
        )
        "Route B - C" -> listOf(
            "Bhayander Station",
            "Kashimira",
            "Vasai Road",
            "Naigaon"
        )
        "Route C - D" -> listOf(
            "Naigaon",
            "Vasai West",
            "Kandivali",
            "Dahisar"
        )
        else -> emptyList()
    }

    val routeDetails = when (route) {
        "Route A - B" -> Pair("15 mins", "5 km")
        "Route B - C" -> Pair("30 mins", "12 km")
        "Route C - D" -> Pair("20 mins", "8 km")
        else -> Pair("", "")
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Details for $route",
                fontSize = 20.sp,
                color = Color(0xFF6200EE)
            )
            // Displaying stops for each route
            Text(text = "Stops:", fontSize = 18.sp, color = Color.Gray)
            stops.forEach { stop ->
                Text(text = "- $stop", fontSize = 16.sp)
            }
            Text(text = "Estimated Duration: ${routeDetails.first}", fontSize = 16.sp)
            Text(text = "Distance: ${routeDetails.second}", fontSize = 16.sp)
        }
    }
}
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddRouteScreen(navController: NavHostController, routes: MutableList<Route>) {
    var routeName by remember { mutableStateOf("") }
    var stops by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Route") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = routeName,
                onValueChange = { routeName = it },
                label = { Text("Route Name") }
            )
            TextField(
                value = stops,
                onValueChange = { stops = it },
                label = { Text("Stops (comma-separated)") }
            )
            TextField(
                value = duration,
                onValueChange = { duration = it },
                label = { Text("Estimated Duration") }
            )
            TextField(
                value = distance,
                onValueChange = { distance = it },
                label = { Text("Distance") }
            )
            Button(onClick = {
                val stopList = stops.split(",").map { it.trim() }
                routes.add(Route(routeName, stopList, duration, distance))
                navController.popBackStack() // Navigate back after adding
            }) {
                Text("Add Route")
            }
        }
    }
}

data class Route(
    val routeName: String,
    val stops: List<String>,
    val duration: String,
    val distance: String
)