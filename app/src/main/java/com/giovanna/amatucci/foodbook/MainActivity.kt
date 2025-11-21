package com.giovanna.amatucci.foodbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.giovanna.amatucci.foodbook.presentation.navigation.AppNavHost
import com.giovanna.amatucci.foodbook.presentation.navigation.content.AnimatedSplashScreen
import com.giovanna.amatucci.foodbook.ui.theme.FoodBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            FoodBookTheme {
                var showAnimatedSplash by remember { mutableStateOf(true) }
                if (showAnimatedSplash) {
                    AnimatedSplashScreen(
                        onAnimationFinished = {
                            showAnimatedSplash = false
                        })
                } else {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}


