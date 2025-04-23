package com.tomifas.TomiPay


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.tomifas.TomiPay.navigation.MainNavigation
import com.tomifas.TomiPay.ui.theme.TomiPayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TomiPayTheme {
                val navController = rememberNavController()

                Surface {
                    MainNavigation(navController = navController)
                }
            }
        }
    }
}