package com.giovanna.amatucci.foodbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.giovanna.amatucci.foodbook.presentation.navigation.AppNavHost
import com.giovanna.amatucci.foodbook.ui.theme.FoodBookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodBookTheme {
                    val navController = rememberNavController()
                    AppNavHost(navController)
                }
        }
    }
}

