package com.giovanna.amatucci.foodbook.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.giovanna.amatucci.foodbook.presentation.authentication.content.AuthRoute
import com.giovanna.amatucci.foodbook.presentation.authentication.content.AuthScreen
import com.giovanna.amatucci.foodbook.presentation.details.content.DetailsRoute
import com.giovanna.amatucci.foodbook.presentation.details.content.DetailsScreen
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.FavoritesViewModel
import com.giovanna.amatucci.foodbook.presentation.navigation.content.MainRoute
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * The main Navigation Host for the application.
 * Defines the navigation graph and manages transitions between screens.
 *
 * @param navController The central [NavHostController] handling navigation.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = AuthGraph
    ) {
        navigation<AuthGraph>(
            startDestination = AuthScreen
        ) {
            composable<AuthScreen> {
                AuthRoute(
                    onNavigateToHome = {
                        navController.navigate(MainGraph) {
                            popUpTo(AuthGraph) { inclusive = true }
                        }
                    }
                )
            }
        }
        composable<MainGraph> { backStackEntry ->
            val mainGraphEntry = remember(backStackEntry) {
                navController.getBackStackEntry<MainGraph>()
            }
            val searchViewModel: SearchViewModel = koinViewModel(
                viewModelStoreOwner = mainGraphEntry
            )

            val favoriteViewModel: FavoritesViewModel = koinViewModel(
                viewModelStoreOwner = mainGraphEntry
            )

            MainRoute(
                searchViewModel = searchViewModel, favoriteViewModel = favoriteViewModel,
                onNavigateToRecipe = { recipeId ->
                    navController.navigate(DetailsScreen(recipeId = recipeId))
                }
            )
        }
        composable<DetailsScreen> {
            DetailsRoute(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}