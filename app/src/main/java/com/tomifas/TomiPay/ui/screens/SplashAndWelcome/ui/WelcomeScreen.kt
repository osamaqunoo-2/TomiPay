package com.tomifas.TomiPay.ui.screens.SplashAndWelcome.ui



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.foundation.BorderStroke

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.tomifas.TomiPay.R
import com.tomifas.TomiPay.navigation.MainNavigation
import com.tomifas.TomiPay.navigation.Screen
import com.tomifas.TomiPay.ui.theme.PrimaryColor
import com.tomifas.TomiPay.ui.theme.TomiPayTheme

@Composable
fun WelcomeScreen(navController: NavController) {
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        val transition = rememberInfiniteTransition()
        val translateAnim = transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val shimmerBrush = Brush.linearGradient(
            colors = listOf(
                Color.Transparent,
                Color.White.copy(alpha = 0.6f),
                Color.Transparent
            ),
            start = Offset(translateAnim.value - 200f, 0f),
            end = Offset(translateAnim.value, 0f)
        )

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "TomiPay Logo",
            modifier = Modifier
                .size(260.dp)
                .graphicsLayer(alpha = 0.99f) // to trigger drawWithCache
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(shimmerBrush, blendMode = BlendMode.SrcOver)
                    }
                }
        )
        Spacer(modifier = Modifier.weight(1f))


        Button(
            onClick = { navController.navigate(Screen.SignUp.route) },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = PrimaryColor
            ),
            border = BorderStroke(1.dp, PrimaryColor),
//            border = androidx.compose.ui.border.BorderStroke(1.dp, Purple40),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp)
        ) {
            Text(text = stringResource(id = R.string.sign_up), fontSize = 16.sp)
        }


        Button(
            onClick = { navController.navigate(Screen.OtpVerificationScreen.route) },
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp)
        ) {
            Text(text = stringResource(id = R.string.login), fontSize = 16.sp)
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    TomiPayTheme {
        val navController = rememberNavController()
        WelcomeScreen(navController)
    }
}