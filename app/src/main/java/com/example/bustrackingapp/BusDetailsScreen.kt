package com.example.bustrackingapp

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BusDetailsScreen(bus: Bus, navController: NavController) {
    var showBookingDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(bus.name) },
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White,
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color(0xFF6200EE),
                contentColor = Color.White
            ) {
                IconButton(onClick = { showBookingDialog = true }) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = "Book Ticket")
                }
                Spacer(modifier = Modifier.weight(1f)) // For spacing between icons
                IconButton(onClick = { /* Implement live tracking logic */ }) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Track Live Location")
                }
                Spacer(modifier = Modifier.weight(1f)) // For spacing between icons
                IconButton(onClick = { /* Implement route sharing logic */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share Route")
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Bus Details",
                fontSize = 24.sp,
                color = Color(0xFF6200EE)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Bus Name: ${bus.name}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Route: ${bus.route}", fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status: ${bus.status}",
                fontSize = 18.sp,
                color = if (bus.status == "On Time") Color(0xFF4CAF50) else Color(0xFFF44336)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Bus Route Map", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            // Display the Bus Route Map
            BusRouteMap(bus)
        }

        // Show Booking Dialog
        if (showBookingDialog) {
            AlertDialog(
                onDismissRequest = { showBookingDialog = false },
                title = { Text("") },
                text = { BookingScreen { showBookingDialog = false } },
                confirmButton = {
                    Button(
                        onClick = { showBookingDialog = false }
                    ) {
                        Text("Close")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showBookingDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun BookingScreen(onBookingConfirmed: () -> Unit) {
    var passengerName by remember { mutableStateOf("") }
    var numberOfTickets by remember { mutableStateOf("1") }

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Book Ticket",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color(0xFF6200EE) // Primary color for the header
        )

        // Passenger Name Input
        OutlinedTextField(
            value = passengerName,
            onValueChange = { passengerName = it },
            label = { Text("Passenger Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Passenger Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color(0xFF6200EE),
                unfocusedLabelColor = Color.Gray
            )
        )

        // Number of Tickets Input
        OutlinedTextField(
            value = numberOfTickets,
            onValueChange = { numberOfTickets = it },
            label = { Text("Number of Tickets") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(Icons.Default.EventSeat, contentDescription = "Number of Tickets") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = Color(0xFF6200EE),
                unfocusedLabelColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Confirm Booking Button
        Button(
            onClick = {
                // Handle booking confirmation logic here
                // For example, save the booking to Firestore
                onBookingConfirmed()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF6200EE))
        ) {
            Text("Confirm Booking", color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cancel Button
        Button(
            onClick = { /* Handle cancel action */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
        ) {
            Text("Cancel", color = Color.White)
        }
    }
}


@Composable
fun BusRouteMap(bus: Bus) {
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(LatLng(19.2654, 72.8681), 13f, 0f, 0f)
    }

    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        cameraPositionState = cameraPositionState
    ) {
        val routePoints = listOf(
            LatLng(19.2853, 72.8690), // Starting point: Bhayander Station
            LatLng(19.2654, 72.8681), // Midpoint: Mira Road Station
            LatLng(19.2384, 72.8542)  // Endpoint: Dahisar Check Naka
        )

        Polyline(
            points = routePoints,
            color = Color.Blue,
            width = 5f
        )

        // Add markers for each point in the route
        routePoints.forEach { point ->
            Marker(
                title = "Bus Stop"
            )
        }
    }
}
