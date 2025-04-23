package com.tomifas.TomiPay.ui.screens.SplashAndWelcome.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.material3.Text
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.tomifas.TomiPay.R
import com.tomifas.TomiPay.navigation.Screen
import com.tomifas.TomiPay.ui.theme.TomiPayTheme

@Composable
fun AppSplashScreen(navController: NavHostController) {
    val scale = remember { Animatable(0.5f) }

    // Animation and navigation effect
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
        delay(2000) // Wait 2 more seconds
        navController.navigate(Screen.Welcome.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "TomiPay Logo",
            modifier = Modifier
                .size(260.dp)
                .scale(scale.value)
        )
    }
}
@Composable
fun SplashContent(scale: Float = 1f) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "TomiPay Logo",
            modifier = Modifier
                .size(260.dp)
                .scale(scale)
        )
    }
}
@Preview
@Composable
fun SplashContentPreview() {
    SplashContent(scale = 1f)
}