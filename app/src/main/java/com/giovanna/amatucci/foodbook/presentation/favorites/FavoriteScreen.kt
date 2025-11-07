package com.giovanna.amatucci.foodbook.presentation.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.components.LoadingIndicator
import com.giovanna.amatucci.foodbook.presentation.components.RecipeList
import org.koin.androidx.compose.koinViewModel

@Composable
fun FavoriteScreen(
    onNavigateToRecipe: (recipeId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = koinViewModel()
) {
    val recipes = viewModel.favoriteRecipes.collectAsLazyPagingItems()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        recipes.loadState.refresh.let { state ->
            when (state) {

                is LoadState.Loading -> {
                    LoadingIndicator()
                }

                is LoadState.Error -> {
                    EmptyMessage(message = stringResource(R.string.favorites_empty_message))
                }

                is LoadState.NotLoading -> {
                    if (recipes.itemCount == 0) {
                        EmptyMessage(message = stringResource(R.string.favorites_empty_message))
                    } else {
                        RecipeList(
                            recipes = recipes, onRecipeClick = onNavigateToRecipe
                        )
                    }
                }
            }
        }
    }
}
