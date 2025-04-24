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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
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
import com.joelkanyi.jcomposecountrycodepicker.component.KomposeCountryCodePicker
import com.joelkanyi.jcomposecountrycodepicker.component.rememberKomposeCountryCodePickerState
import com.tomifas.TomiPay.R
import com.tomifas.TomiPay.navigation.Screen
import com.tomifas.TomiPay.ui.theme.PrimaryColor
import com.tomifas.TomiPay.ui.theme.TomiPayTheme
import com.tomifas.TomiPay.ui.theme.hintText
import com.tomifas.TomiPay.ui.theme.lineOutlinColor
import com.tomifas.TomiPay.utils.SecureStorageUtils
import com.tomifas.TomiPay.viewmodel.AuthViewModel
import org.json.JSONObject

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun LoginScreen(navController: NavHostController) {
    var phoneNumber by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val viewModel: AuthViewModel = viewModel()

    val sendOtpResponse by viewModel.sendOtpResponse.collectAsState()



    val loading by viewModel.loading.collectAsState()

    val loginResponse by viewModel.loginResponse.collectAsState()

    val pickerState = rememberKomposeCountryCodePickerState(
        showCountryCode = true,
        showCountryFlag = true,
        defaultCountryCode = "PS"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

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

        Text(stringResource(id = R.string.login), fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(42.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            if (phoneNumber.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.phone_number),
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp, top = 14.dp)
                )
            }

            KomposeCountryCodePicker(
                state = pickerState,
                text = phoneNumber,
                onValueChange = { phoneNumber = it },
                modifier = Modifier.fillMaxWidth(),

                )
        }

        var showSheet by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { showSheet = true }) {
                Text(
                    text = stringResource(R.string.open_reset_sheet),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            ResetPasswordBottomSheet(phoneNumber,
                showSheet = showSheet,viewModel,
                onDismiss = { showSheet = false },
                onSend = {

                    showSheet = false
                }
            )
        }

        Button(
            onClick = {
                showError = phoneNumber.isBlank()
                if (!showError) {
                    // Action to send code or navigate
                    viewModel.loginUser(context,phoneNumber.trim())

                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
            } else {
                Text(stringResource(id = R.string.send_code), color = Color.White)
            }

        }
        loginResponse?.let { response ->
            LaunchedEffect(response) {


                when (response.code()) {
                    200 -> {
                        val msg = response.body()?.message ?: "Success"

                        val user = response.body()?.user
//                        val token = response.body()?.token ?: ""


//                        navController.navigate(
//                            Screen.MainScreen.createRoute(
//                                phone = phoneNumber.trim(),
//                                source = "login"
//                            )
//                        )


//                        SecureStorageUtils.saveToken(context, token)
                        user?.let {
                            SecureStorageUtils.saveUser(context, it)
                        }

                        navController.navigate(Screen.MainScreen.route)

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
        sendOtpResponse?.let { response ->
            LaunchedEffect(response) {

                when (response.code()) {
                    200 -> {

                        navController.navigate(
                            Screen.OtpVerificationScreen.createRoute(
                                phone = phoneNumber.trim(),
                                source = "forgot"
                            )
                        )


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


        if (showError) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.Hint_of_req),
                color = Color.Red,
                fontSize = 14.sp
            )
        }


        Spacer(modifier = Modifier.weight(1f))




        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.Dontalready_have_account),
                color = hintText,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.sign_up),
                color = PrimaryColor,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.SignUp.route)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordBottomSheet(
    phone: String,
    showSheet: Boolean,
    viewModel: AuthViewModel,
    onDismiss: () -> Unit,
    onSend: () -> Unit
) {
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.reset_password_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text(stringResource(R.string.cancel_button))
                    }

                    Button(
                        onClick = {
                            viewModel.sendOtpForLogin(phone.trim())
                        },

                    ) {
                        Text(stringResource(R.string.send_button))
                    }


                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    TomiPayTheme {
        val navController = rememberNavController()
//        LoginScreen(navController)
    }
}
