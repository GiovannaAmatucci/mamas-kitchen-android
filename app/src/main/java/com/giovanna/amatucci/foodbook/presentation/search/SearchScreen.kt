package com.giovanna.amatucci.foodbook.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.components.PagingStateHandler
import com.giovanna.amatucci.foodbook.presentation.components.RecipeList

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


@Composable
private fun SearchContent(
    searchQuery: String,
    recipes: LazyPagingItems<RecipeItem>,
    onRecipeClick: (String) -> Unit
) {
    if (recipes.itemCount == 0 && searchQuery.isBlank()) {
        EmptyMessage(stringResource(R.string.search_idle_message))
    } else {
        PagingStateHandler(
            pagingItems = recipes, emptyContent = {
                if (searchQuery.isNotBlank()) {
                    EmptyMessage(stringResource(R.string.search_empty_message, searchQuery))
                }
            }) { loadedRecipes ->
            RecipeList(recipes = loadedRecipes, onRecipeClick = onRecipeClick)
        }
    }
}


