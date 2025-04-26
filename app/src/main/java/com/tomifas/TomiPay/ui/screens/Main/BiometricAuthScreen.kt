package com.tomifas.TomiPay.ui.screens.Main

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import com.tomifas.TomiPay.utils.SecureStorageUtils
import java.util.concurrent.Executor
@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun BiometricAuthScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context.findActivity()
    var showBiometric by remember { mutableStateOf(true) }

    androidx.compose.material3.Surface {
        androidx.compose.material3.Text(
            text = "Authenticating with fingerprint...")
    }



    if (showBiometric && activity != null) {

        LaunchedEffect(Unit) {
            showBiometricPrompt(
                activity = activity as FragmentActivity,
                onAuthenticated = {
                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show()
//                    SecureStorageUtils.savePhoneNumber(context, phoneNumber)

                    SecureStorageUtils.enableBiometric(context)
                    navController.popBackStack()
                },
                onFailed = {
                    Toast.makeText(context, "Fingerprint not recognized", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                },
                onCancelled = {
                    Toast.makeText(context, "Authentication cancelled", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            )
        }
        showBiometric = false
    }
}
fun showBiometricPrompt(
    activity: FragmentActivity, // 🛠️ لازم FragmentActivity
    onAuthenticated: () -> Unit,
    onFailed: () -> Unit,
    onCancelled: () -> Unit
) {
    val executor = ContextCompat.getMainExecutor(activity)


    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("BiometricAuth", "❗ Authentication Error: $errString (code: $errorCode)")
                onCancelled()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Log.d("BiometricAuth", "✅ Authentication Succeeded!")
                onAuthenticated()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d("BiometricAuth", "❌ Authentication Failed (fingerprint not recognized)")
                onFailed()
            }
        }
    )
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Authenticate using your biometric credential")
        .setNegativeButtonText("Cancel")
        .build()

    Log.d("BiometricAuth", "🚀 Showing biometric prompt now...") // ← هنا تطبع قبل عرض البصمة

    biometricPrompt.authenticate(promptInfo)
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
