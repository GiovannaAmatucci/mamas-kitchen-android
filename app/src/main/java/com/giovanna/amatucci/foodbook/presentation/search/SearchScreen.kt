package com.giovanna.amatucci.foodbook.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.components.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.components.PagingStateHandler
import com.giovanna.amatucci.foodbook.presentation.components.RecipeList
import com.giovanna.amatucci.foodbook.presentation.components.SearchNavTopAppBar
import com.giovanna.amatucci.foodbook.ui.theme.Dimens

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


