package com.giovanna.amatucci.foodbook.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.giovanna.amatucci.foodbook.presentation.authentication.AuthScreen
import com.giovanna.amatucci.foodbook.presentation.details.DetailsScreen
import com.giovanna.amatucci.foodbook.presentation.search.SearchScreen

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = AuthGraph
    ) {
        navigation<AuthGraph>(
            startDestination = AuthScreen
        ) {
            composable<AuthScreen> {
                AuthScreen(
                    onNavigateToHome = {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
        navigation<MainGraph>(
            startDestination = SearchScreen
        ) {
            composable<SearchScreen> {
                SearchScreen(
                    onNavigateToRecipe = { recipeId ->
                        navController.navigate(DetailsScreen(recipeId = recipeId))
                    }
                )
            }

            composable<DetailsScreen> {
                DetailsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}