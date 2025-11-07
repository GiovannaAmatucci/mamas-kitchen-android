package com.giovanna.amatucci.foodbook.presentation.navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.giovanna.amatucci.foodbook.presentation.authentication.AuthScreen
import com.giovanna.amatucci.foodbook.presentation.details.DetailsScreen
import com.giovanna.amatucci.foodbook.presentation.main.MainScreen
import com.giovanna.amatucci.foodbook.presentation.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
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

            MainScreen(
                searchViewModel = searchViewModel,
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