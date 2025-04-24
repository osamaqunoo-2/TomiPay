package com.tomifas.TomiPay.ui.screens.Main

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.tomifas.TomiPay.R
import com.tomifas.TomiPay.navigation.Screen
import com.tomifas.TomiPay.ui.theme.TomiPayTheme
import com.tomifas.TomiPay.utils.SecureStorageUtils

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val user = SecureStorageUtils.getUser(context)



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.welcome_user, user?.first_name ?: "User"))


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.home_screen_message),

            )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                SecureStorageUtils.logout(context)
                navController.navigate(Screen.Splash.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = stringResource(id = R.string.logout), color = Color.White)
        }
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "مرحبًا بك أسامة",


            )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "أنت الآن في الواجهة الرئيسية",
        )
    }
}

@Preview
@Composable
fun MainScreenPrivew() {
    HomeScreen()
}