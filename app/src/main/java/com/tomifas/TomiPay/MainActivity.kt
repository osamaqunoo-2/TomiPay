package com.tomifas.TomiPay


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.compose.rememberNavController
import com.tomifas.TomiPay.navigation.MainNavigation
import com.tomifas.TomiPay.ui.theme.TomiPayTheme
import java.util.Locale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentLocale = Locale.getDefault()
            val isRtl = currentLocale.language == "ar"

            CompositionLocalProvider(
                LocalLayoutDirection provides if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
            ) {
                TomiPayTheme {
                    val navController = rememberNavController()

                    Surface {
                        MainNavigation(navController = navController)
                    }
                }
            }
        }
    }
}