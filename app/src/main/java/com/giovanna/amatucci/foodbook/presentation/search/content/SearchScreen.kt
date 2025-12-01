package com.giovanna.amatucci.foodbook.presentation.search.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.components.NetworkFailedComposable
import com.giovanna.amatucci.foodbook.presentation.components.PagingStateComposable
import com.giovanna.amatucci.foodbook.presentation.components.RecipeList
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchUiState
import com.giovanna.amatucci.foodbook.util.constants.UiConstants

/**
 * The main content area for the Search screen.
 * Collects the paging data from the state and delegates rendering to [SearchContent].
 *
 * @param onNavigateToRecipe Callback triggered when a recipe is clicked.
 * @param state The current UI state containing the search query and recipe flow.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateToRecipe: (id: String) -> Unit, state: SearchUiState
) {
    val uiState = state.recipes.collectAsLazyPagingItems()
    Column(modifier = Modifier.fillMaxSize()) {
        SearchContent(
            searchQuery = state.submittedQuery,
            recipes = uiState,
            onRecipeClick = onNavigateToRecipe
        )
    }
}

/**
 * Handles the logic for displaying different states of the search results:
 * - Idle (no search yet)
 * - Loading/Error (handled by [PagingStateComposable])
 * - Empty results
 * - Success list
 *
 * @param searchQuery The query string currently submitted.
 * @param recipes The paginated list of recipes.
 * @param onRecipeClick Callback triggered when a recipe card is clicked.
 */
@Composable
private fun SearchContent(
    searchQuery: String,
    recipes: LazyPagingItems<RecipeItem>,
    onRecipeClick: (String) -> Unit
) {
    if (recipes.itemCount == UiConstants.SEARCH_ROUTE_CONTENT_ITEM_COUNT && searchQuery.isBlank()) EmptyMessage(
        stringResource(R.string.search_idle_message)
    )
    else PagingStateComposable(pagingItems = recipes, emptyContent = {
        if (searchQuery.isNotBlank()) {
            EmptyMessage(stringResource(R.string.search_empty_message, searchQuery))
        }
    }, errorContent = {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            NetworkFailedComposable(
                errorMessage = stringResource(R.string.search_error_message_loading_failed),
                onRetry = { recipes.retry() })
        }
    }) { loadedRecipes ->
        RecipeList(recipes = loadedRecipes, onRecipeClick = onRecipeClick)
    }
}








