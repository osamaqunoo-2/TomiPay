package com.tomifas.TomiPay.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tomifas.TomiPay.ui.screens.SplashAndWelcome.ui.AppSplashScreen
import com.tomifas.TomiPay.ui.screens.SplashAndWelcome.ui.WelcomeScreen
import com.tomifas.TomiPay.ui.screens.Auth.LoginScreen
import com.tomifas.TomiPay.ui.screens.Auth.OtpVerificationScreen
import com.tomifas.TomiPay.ui.screens.Auth.SignUpScreen


sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object OtpVerificationScreen : Screen("OtpVerificationScreen")
}

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            AppSplashScreen(navController)
        }
        composable(Screen.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.SignUp.route) {
            SignUpScreen(navController)
        }
        composable(Screen.OtpVerificationScreen.route) {
            OtpVerificationScreen(navController)
        }
    }
}