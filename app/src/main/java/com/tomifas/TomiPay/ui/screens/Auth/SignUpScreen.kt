package com.tomifas.TomiPay.ui.screens.Auth

import android.widget.Toast
import android.util.Log

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import com.tomifas.TomiPay.viewmodel.AuthViewModel
import org.json.JSONObject

@Composable
fun SignUpScreen(navController: NavHostController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val viewModel: AuthViewModel = viewModel()
    val loading by viewModel.loading.collectAsState()
    val registerResponse by viewModel.registerResponse.collectAsState()

    val context = LocalContext.current


    val pickerState = rememberKomposeCountryCodePickerState(
        showCountryCode = true,
        showCountryFlag = true,
        defaultCountryCode = "PS"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
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

        Text("Sign Up", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text(stringResource(id = R.string.first_name)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text(stringResource(id = R.string.last_name)) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            if (phoneNumber.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.phone_number),
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 16.dp, top = 14.dp)
                )
            }

            Surface(
                color = Color.White,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFFCCCCCC)),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                KomposeCountryCodePicker(
                    state = pickerState,
                    text = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        errorContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                    )
                )
            }


        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                showError = firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()
                if (!showError) {



                    viewModel.registerUser(
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        phone = phoneNumber.trim()
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
            } else {
                Text(stringResource(id = R.string.send_code), color = Color.White)
            }
        }
        registerResponse?.let { response ->
            LaunchedEffect(response) {
                when (response.code()) {
                    200 -> {


                        val msg = response.body()?.message ?: "Success"
                        navController.navigate(
                            Screen.OtpVerificationScreen.createRoute(
                                phone = phoneNumber.trim(),
                                source = "register"
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)
        ) {
            Text(
                text = stringResource(id = R.string.already_have_account),
                color = hintText,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.login),
                color = PrimaryColor,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    TomiPayTheme {
        val navController = rememberNavController()
        SignUpScreen(navController)
    }
}
