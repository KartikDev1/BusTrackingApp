package com.example.bustrackingapp

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var alpha by remember { mutableStateOf(0f) }

    // Animate the alpha value for fade-in effect
    val alphaAnimation = animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(durationMillis = 1500, easing = FastOutSlowInEasing)
    )

    // Use LaunchedEffect to manage the timing of the splash screen
    LaunchedEffect(Unit) {
        alpha = 1f // Start with invisible and fade in
        delay(2000) // Show for 2 seconds
        onSplashFinished() // Transition to HomeScreen
    }

    // Box to hold the background color and content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF6200EE)), // Set the background color to yellow
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Image icon above the text
            Image(
                painter = painterResource(id = R.drawable.home_bus), // Replace with your icon resource
                contentDescription = "Splash Icon",
                modifier = Modifier.size(100.dp) // Adjust size as needed
            )
            Spacer(modifier = Modifier.height(16.dp)) // Space between icon and text
            Text(
                text = "Ready to Ride? Let's Go!", // Customize this text
                fontSize = 30.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = alphaAnimation.value), // Fade effect on text
                modifier = Modifier.alpha(alphaAnimation.value) // Fade in effect
            )
        }
    }
}
