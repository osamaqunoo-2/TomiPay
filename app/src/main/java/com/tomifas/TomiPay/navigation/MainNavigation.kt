package com.tomifas.TomiPay.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tomifas.TomiPay.ui.screens.SplashAndWelcome.ui.AppSplashScreen
import com.tomifas.TomiPay.ui.screens.SplashAndWelcome.ui.WelcomeScreen
import com.tomifas.TomiPay.ui.screens.Auth.LoginScreen
import com.tomifas.TomiPay.ui.screens.Auth.OtpVerificationScreen
import com.tomifas.TomiPay.ui.screens.Auth.SignUpScreen
import com.tomifas.TomiPay.ui.screens.Auth.CreateNewPasswordScreen
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.tomifas.TomiPay.ui.screens.Main.MainScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object MainScreen : Screen("MainScreen")
//    object OtpVerificationScreen : Screen("OtpVerificationScreen")
//    object CreateNewPasswordScreen : Screen("CreateNewPasswordScreen")
    object OtpVerificationScreen : Screen("otp_verification/{phone}/{source}") {
        fun createRoute(phone: String, source: String): String = "otp_verification/$phone/$source"
    }
    object CreateNewPasswordScreen : Screen("CreateNewPasswordScreen/{phone}/{otp}") {
        fun createRoute(phone: String, otp: String): String = "CreateNewPasswordScreen/$phone/$otp"
    }
}

@RequiresApi(Build.VERSION_CODES.M)
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
        composable(Screen.MainScreen.route) {
            MainScreen(navController)
        }
//        composable(Screen.OtpVerificationScreen.route) {
//            OtpVerificationScreen(navController)
//        }
//        composable(Screen.CreateNewPasswordScreen.route) {
//            CreateNewPasswordScreen(navController)
//        }
        composable(
            route = Screen.OtpVerificationScreen.route,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("source") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val source = backStackEntry.arguments?.getString("source") ?: ""

            OtpVerificationScreen(navController = navController, phone = phone, source = source)
        }

        composable(
            route = Screen.CreateNewPasswordScreen.route,
            arguments = listOf(
                navArgument("phone") { type = NavType.StringType },
                navArgument("otp") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val otp = backStackEntry.arguments?.getString("otp") ?: ""

            CreateNewPasswordScreen(navController = navController, phone = phone, otp = otp)
        }
    }
}