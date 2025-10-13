package com.giovanna.amatucci.foodbook.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.giovanna.amatucci.foodbook.presentation.search.SearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = SearchScreen, modifier = Modifier
    ) {
        composable<SearchScreen> {
            SearchScreen()
        }
    }
}