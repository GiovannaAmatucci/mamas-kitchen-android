package com.giovanna.amatucci.foodbook.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.componets.EmptyMessage
import com.giovanna.amatucci.foodbook.presentation.componets.LoadingIndicator
import com.giovanna.amatucci.foodbook.presentation.componets.RecipeList
import org.koin.androidx.compose.koinViewModel


@Composable
fun SearchScreen(
    onNavigateToRecipe: (id: String) -> Unit, viewModel: SearchViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recipes = uiState.recipes.collectAsLazyPagingItems()


    SearchContent(
        searchQuery = uiState.searchQuery,
        onQueryChange = { query -> viewModel.onEvent(SearchEvent.OnSearchQueryChange(query)) },
        recipes = recipes,
        onRecipeClick = onNavigateToRecipe
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchContent(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    recipes: LazyPagingItems<RecipeItem>,
    onRecipeClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_screen_title)) })
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onQueryChange,
                label = { Text(stringResource(R.string.search_screen_title)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                val isSearchQueryEmpty = searchQuery.isBlank()
                val isInitialLoad =
                    recipes.loadState.refresh is LoadState.Loading && recipes.itemCount == 0
                val isEmptyResult =
                    recipes.loadState.refresh is LoadState.NotLoading && recipes.itemCount == 0

                when {
                    isSearchQueryEmpty -> {
                        EmptyMessage(stringResource(R.string.search_idle_message))
                    }

                    isInitialLoad -> {
                        LoadingIndicator()
                    }

                    // 3. Se deu erro no refresh, mostre erro (pode ser a mesma mensagem de vazio).
                    recipes.loadState.refresh is LoadState.Error -> {
                        EmptyMessage(stringResource(R.string.search_error_message_loading_failed))
                    }

                    isEmptyResult -> {
                        EmptyMessage(stringResource(R.string.search_empty_message, searchQuery))
                    }

                    else -> {
                        RecipeList(recipes = recipes, onRecipeClick = onRecipeClick)
                    }
                }
            }
        }
    }
}