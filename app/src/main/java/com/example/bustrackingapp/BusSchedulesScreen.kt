package com.example.bustrackingapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

data class BusSchedule(
    val busName: String,
    val departureTime: String,
    val arrivalTime: String,
    val status: String
)

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BusSchedulesScreen(navController: NavHostController) {
    // Expanded list of bus schedules
    val schedules = listOf(
        BusSchedule("Bus 101", "08:00 AM", "09:00 AM", "On-Time"),
        BusSchedule("Bus 102", "09:15 AM", "10:30 AM", "Delayed"),
        BusSchedule("Bus 103", "11:00 AM", "12:00 PM", "Arrived"),
        BusSchedule("Bus 201", "07:30 AM", "08:45 AM", "On-Time"),
        BusSchedule("Bus 202", "10:00 AM", "11:15 AM", "Delayed"),
        BusSchedule("Bus 203", "12:30 PM", "01:45 PM", "On-Time"),
        BusSchedule("Bus 301", "02:00 PM", "03:15 PM", "On-Time"),
        BusSchedule("Bus 302", "03:30 PM", "04:45 PM", "Delayed"),
        BusSchedule("Bus 303", "05:00 PM", "06:15 PM", "Arrived"),
        BusSchedule("Bus 401", "06:30 PM", "07:45 PM", "On-Time"),
        BusSchedule("Bus 402", "08:00 PM", "09:15 PM", "On-Time"),
        BusSchedule("Bus 403", "09:30 PM", "10:30 PM", "Arrived")
    )

    val favorites = remember { mutableStateListOf<String>() }
    var selectedSort by remember { mutableStateOf("Departure Time") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bus Schedules") },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    SortDropdown(selectedSort) { newSort -> selectedSort = newSort }
                }
            )
        }
    ) {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(schedules.sortedBy { if (selectedSort == "Departure Time") it.departureTime else it.busName }) { schedule ->
                val isFavorite = favorites.contains(schedule.busName)
                ScheduleCard(
                    schedule = schedule,
                    isFavorite = isFavorite,
                    onToggleFavorite = {
                        if (isFavorite) favorites.remove(schedule.busName)
                        else favorites.add(schedule.busName)
                    }
                )
            }
        }
    }
}

@Composable
fun SortDropdown(selectedSort: String, onSortChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(selectedSort)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("Departure Time", "Bus Name").forEach { sortOption ->
                DropdownMenuItem(onClick = {
                    onSortChange(sortOption)
                    expanded = false
                }) {
                    Text(sortOption)
                }
            }
        }
    }
}

@Composable
fun ScheduleCard(
    schedule: BusSchedule,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        elevation = 6.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DirectionsBus, contentDescription = "Bus Icon", tint = Color(0xFF6200EE))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = schedule.busName, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                FavoriteIcon(isFavorite, onToggleFavorite)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "Departure: ${schedule.departureTime}", fontSize = 16.sp, color = Color.Gray)
                Text(text = "Arrival: ${schedule.arrivalTime}", fontSize = 16.sp, color = Color.Gray)
                StatusChip(schedule.status)
            }
        }
    }
}

@Composable
fun FavoriteIcon(isFavorite: Boolean, onToggleFavorite: () -> Unit) {
    IconButton(onClick = onToggleFavorite) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = "Favorite",
            tint = if (isFavorite) Color.Yellow else Color.Gray
        )
    }
}

@Composable
fun StatusChip(status: String) {
    val color = when (status) {
        "On-Time" -> Color(0xFF4CAF50)
        "Delayed" -> Color(0xFFFF9800)
        "Arrived" -> Color(0xFF2196F3)
        else -> Color.Gray
    }

    Surface(
        shape = MaterialTheme.shapes.small,
        color = color,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = status,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

fun openGoogleMaps(route: String, context: Context) {
    val uri = Uri.parse("https://www.google.com/maps/dir/$route")
    val mapIntent = Intent(Intent.ACTION_VIEW, uri)
    mapIntent.setPackage("com.google.android.apps.maps")
    context.startActivity(mapIntent)
}
