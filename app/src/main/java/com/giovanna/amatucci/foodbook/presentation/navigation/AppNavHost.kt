package com.giovanna.amatucci.foodbook.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.giovanna.amatucci.foodbook.presentation.details.DetailsScreen
import com.giovanna.amatucci.foodbook.presentation.details.DetailsViewModel
import com.giovanna.amatucci.foodbook.presentation.search.SearchScreen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = SearchScreen, modifier = Modifier
    ) {
        composable<SearchScreen> {
            SearchScreen(
                onNavigateToRecipe = { id ->
                    navController.navigate(DetailsScreen(recipeId = id))
                }
            )
        }
        composable<DetailsScreen> { backStackEntry ->
            val args: DetailsScreen = backStackEntry.toRoute()
            val oIdDaReceita = args.recipeId

            val detailsViewModel: DetailsViewModel = koinViewModel(
                parameters = { parametersOf(oIdDaReceita) }
            )
            DetailsScreen(
                onNavigateBack = { navController.popBackStack() },
                viewModel = detailsViewModel
            )
        }
    }
}