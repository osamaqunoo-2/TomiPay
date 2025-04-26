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
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.core.content.ContextCompat
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.util.concurrent.Executor


@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val user = SecureStorageUtils.getUser(context)

//    SecureStorageUtils.disableBiometric(context)

    var isBiometricEnabled = SecureStorageUtils.isBiometricEnabled(context) // اضيف فحص البصمة
    var showBiometricPrompt by remember { mutableStateOf(false) }

    var showBottomSheet by remember { mutableStateOf(false) }





    LaunchedEffect(Unit) {

        if (!isBiometricEnabled) {


            showBottomSheet = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(R.string.welcome_user, user?.first_name ?: "User"))
        SecureStorageUtils.savePhoneNumber(context, user?.phone!!)
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
        Spacer(modifier = Modifier.height(32.dp))






        Button(
            onClick = {
                if (isBiometricEnabled) {

                    SecureStorageUtils.disableBiometric(context)
                    SecureStorageUtils.clearPhoneNumber(context)
                    isBiometricEnabled = false
                    Toast.makeText(context, "Biometric disabled successfully.", Toast.LENGTH_SHORT).show()
                } else {

                    showBottomSheet = true
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(
                text = if (isBiometricEnabled) "Disable Biometric" else "Enable Biometric",
                color = Color.White
            )
        }
    }


    if (showBottomSheet) {
        BiometricPromptBottomSheet(
            navController = navController,
            onDismiss = {
                showBottomSheet = false

                isBiometricEnabled = SecureStorageUtils.isBiometricEnabled(context)
            }
        )


    }


//
//    if (showBottomSheet) {
//        BiometricPromptBottomSheet(
//            navController = navController,
//            onDismiss = {
//                showBottomSheet = false
//                showBiometricPrompt = false
//            }
//        )
//    }
//
//    if (showBiometricPrompt) {
//
//        BiometricPromptBottomSheet(
//            navController = navController,
//            onDismiss = { showBiometricPrompt = false }
//        )
//    }
}

@Composable
fun BiometricPromptBottomSheet(
    navController: NavHostController,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.biometric_prompt_title)) },
        text = { Text(text = stringResource(id = R.string.biometric_prompt_message)) },
        confirmButton = {
            Button(onClick = {

                navController.navigate(Screen.BiometricAuthScreen.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
                onDismiss()
            }) {
                Text(text = stringResource(id = R.string.biometric_prompt_confirm))
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.biometric_prompt_later))
            }
        }
    )
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