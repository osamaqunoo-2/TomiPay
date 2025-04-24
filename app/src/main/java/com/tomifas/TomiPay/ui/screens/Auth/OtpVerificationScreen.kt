package com.tomifas.TomiPay.ui.screens.Auth

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tomifas.TomiPay.R
import com.tomifas.TomiPay.navigation.Screen
import com.tomifas.TomiPay.ui.theme.PrimaryColor
import com.tomifas.TomiPay.ui.theme.TomiPayTheme
import com.tomifas.TomiPay.ui.theme.lineOutlinColor
import com.tomifas.TomiPay.utils.SecureStorageUtils
import com.tomifas.TomiPay.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun OtpVerificationScreen(phone: String, source: String, navController: NavHostController) {
    var code by remember { mutableStateOf("") }
    var timer by remember { mutableStateOf(60) }
    var canResend by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val viewModel: AuthViewModel = viewModel()
    val loading by viewModel.loading.collectAsState()
    val verifyResponse by viewModel.verifyResponse.collectAsState()
    val sendOtpResponse by viewModel.sendOtpResponse.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(key1 = timer) {
        if (timer > 0) {
            delay(1000)
            timer--
        } else {
            canResend = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                stringResource(id = R.string.Back),
                color = PrimaryColor,
                modifier = Modifier.clickable { navController.popBackStack() }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
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
                .size(190.dp)
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(shimmerBrush, blendMode = BlendMode.SrcOver)
                    }
                }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.OTP_Verification),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { if (it.length <= 6) code = it },

            label = { Text(stringResource(id = R.string.Enter_digit_code)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = lineOutlinColor,
                unfocusedIndicatorColor = lineOutlinColor,
                focusedLabelColor = PrimaryColor,
                unfocusedLabelColor = lineOutlinColor,
                cursorColor = lineOutlinColor
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                showError = code.isBlank()
                if (!showError) {
                    // Action to send code or navigate
                    viewModel.verifyOtp(
                        otp = code.trim(),
                        phone = phone.trim()
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(stringResource(id = R.string.Verify), color = Color.White)
            }
        }
        verifyResponse?.let { response ->
            LaunchedEffect(response) {

                val tag = "otp_OTP_RESPONSE"
                Log.d(tag, "Raw response: $response")
                Log.d(tag, "Phone: ${phone.trim()}")
                Log.d(tag, "Code: ${response.code()}")
                Log.d(tag, "Body: ${response.body()}")
                Log.d(tag, "Error: ${response.errorBody()?.string()}")


                when (response.code()) {
                    200 -> {

                        val token = response.body()?.token ?: ""

                        SecureStorageUtils.saveToken(context, token)

                        if (source.equals("forgot")) {

                            navController.navigate(
                                Screen.CreateNewPasswordScreen.createRoute(
                                    phone = phone.trim(),
                                    otp = code.trim(),
                                )
                            )

                        } else {
                            val user = response.body()?.user
//
                            user?.let {
                                SecureStorageUtils.saveUser(context, it)
                            }
                            navController.navigate(Screen.MainScreen.route)

                        }


                    }


                    else -> {
                        val errorMessage = try {
                            val errorJson = JSONObject(response.errorBody()?.string() ?: "")
                            errorJson.getString("message")
                        } catch (e: Exception) {
                            context.getString(R.string.default_error_message, response.code())

                        }

                        Toast.makeText(
                            context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (canResend) {
            Text(
                text = stringResource(id = R.string.Resend_code),
                color = PrimaryColor,
                modifier = Modifier.clickable {
                    canResend = false
                    timer = 60
                    // TODO: resend code logic
                    viewModel.sendOtpForLogin(phone.trim())


                }
            )
        } else {
            Text(text = stringResource(id = R.string.resend_in_seconds, timer), color = Color.Gray)
        }

        sendOtpResponse?.let { response ->
            LaunchedEffect(response) {


//                val tag = "sendOtp_RESPONSE"
//                Log.d(tag, "Raw response: $response")
//                Log.d(tag, "Phone: ${phone.trim()}")
//                Log.d(tag, "Code: ${response.code()}")
//                Log.d(tag, "Body: ${response.body()}")
//                Log.d(tag, "Error: ${response.errorBody()?.string()}")
//

                when (response.code()) {
                    200 -> {

                        Toast.makeText(
                            context,
                            context.getString(R.string.otp_send_success),
                            Toast.LENGTH_SHORT
                        ).show()


                    }

                    else -> {
                        val errorMessage = try {
                            val errorJson = JSONObject(response.errorBody()?.string() ?: "")
                            errorJson.getString("message")
                        } catch (e: Exception) {
                            context.getString(R.string.default_error_message, response.code())

                        }

                        Toast.makeText(
                            context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OtpVerificationScreenPreviw() {
    TomiPayTheme {
        val navController = rememberNavController()
//        OtpVerificationScreen("n", "a", navController)
    }
}
