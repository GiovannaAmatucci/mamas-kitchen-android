package com.giovanna.amatucci.foodbook.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.giovanna.amatucci.foodbook.presentation.authentication.content.AuthRoute
import com.giovanna.amatucci.foodbook.presentation.details.content.DetailsRoute
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.FavoritesViewModel
import com.giovanna.amatucci.foodbook.presentation.navigation.content.MainRoute
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.SearchViewModel
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import org.koin.androidx.compose.koinViewModel

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
            val searchViewModel: SearchViewModel = koinViewModel(viewModelStoreOwner = backStackEntry)
            val favoriteViewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = backStackEntry)
            MainRoute(
                searchViewModel = searchViewModel,
                favoriteViewModel = favoriteViewModel,
                onNavigateToRecipe = { recipeId ->
                    navController.navigate(DetailsScreen(recipeId = recipeId))
                }
            )
        }
        composable<DetailsScreen> {
            val mainGraphEntry = remember(it) { navController.getBackStackEntry<MainGraph>() }
            val searchViewModel: SearchViewModel = koinViewModel(viewModelStoreOwner = mainGraphEntry)
            DetailsRoute(
                onNavigateBack = { navController.popBackStack() },
                onSearchCategory = { query ->
                    searchViewModel.onEvent(SearchEvent.UpdateSearchQuery(query))
                    searchViewModel.onEvent(SearchEvent.SubmitSearch(query))
                    navController.popBackStack<MainGraph>(inclusive = false)
                }
            )
        }
    }
}