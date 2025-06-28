package com.example.bustrackingapp

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

data class Bus(
    val id: Int,
    val name: String,
    val route: String,
    val status: String // Could be "On Time", "Delayed", etc.
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    val scaffoldState = rememberScaffoldState(drawerState = rememberDrawerState(DrawerValue.Closed))
    val coroutineScope = rememberCoroutineScope()

    val buses = listOf(
        Bus(1, "Bus 101", "Route A - B", "On Time"),
        Bus(2, "Bus 102", "Route B - C", "Delayed"),
        Bus(3, "Bus 103", "Route C - D", "On Time")
    )

    // Filter buses based on search query
    val filteredBuses = buses.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.route.contains(searchQuery, ignoreCase = true) ||
                it.status.contains(searchQuery, ignoreCase = true)
    }

    // Define colors for the light theme
    val backgroundColor = Color.White
    val textColor = Color.Black

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Bus Tracking App", fontSize = 20.sp) },
                    backgroundColor = Color(0xFF6200EE),
                    contentColor = Color.White,
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                scaffoldState.drawerState.open()
                            }
                        }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )

                // Search Bar
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search buses...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White
                    )
                )
            }
        },
        drawerContent = {
            DrawerMenu(
                onHomeClick = { coroutineScope.launch { scaffoldState.drawerState.close() } },
                onSavedRoutesClick = {
                    coroutineScope.launch {
                        navController.navigate("saved_routes_screen")
                        scaffoldState.drawerState.close()
                    }
                },
                onBusSchedulesClick = {
                    coroutineScope.launch {
                        navController.navigate("bus_schedules_screen")
                        scaffoldState.drawerState.close()
                    }
                },
                onAboutClick = {
                    coroutineScope.launch {
                        navController.navigate("about_screen")
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .background(backgroundColor)
                .fillMaxSize()
        ) {
            items(filteredBuses.size) { index ->
                BusCard(filteredBuses[index], navController, textColor)
            }
        }
    }
}

@Composable
fun DrawerMenu(
    onHomeClick: () -> Unit,
    onSavedRoutesClick: () -> Unit,
    onBusSchedulesClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))

        Spacer(modifier = Modifier.height(16.dp))

        DrawerItem(icon = Icons.Default.Home, title = "Home", onClick = onHomeClick)
        DrawerItem(icon = Icons.Default.Star, title = "Saved Routes", onClick = onSavedRoutesClick)
        DrawerItem(icon = Icons.Default.List, title = "Bus Schedules", onClick = onBusSchedulesClick)
        DrawerItem(icon = Icons.Default.Info, title = "About", onClick = onAboutClick)
    }
}

@Composable
fun DrawerItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title)
    }
}

@Composable
fun BusCard(bus: Bus, navController: NavHostController, textColor: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("bus_details_screen/${bus.id}") }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = 6.dp,
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.icon_bus), // Replace with your bus icon resource
                contentDescription = "Bus Icon",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = bus.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Route: ${bus.route}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Status: ${bus.status}",
                    fontSize = 14.sp,
                    color = if (bus.status == "On Time") Color(0xFF4CAF50) else Color(0xFFF44336)
                )
            }
        }
    }
}
