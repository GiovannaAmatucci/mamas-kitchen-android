package com.giovanna.amatucci.foodbook.presentation.navigation.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.FavoritesViewModel
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.SearchViewModel

@Composable
fun MainRoute(
    searchViewModel: SearchViewModel,
    favoriteViewModel: FavoritesViewModel,
    onNavigateToRecipe: (String) -> Unit,
) {
    val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
    val favoritesUiState by favoriteViewModel.uiState.collectAsStateWithLifecycle()

    MainScreen(
        searchUiState = searchUiState,
        favoritesUiState = favoritesUiState,
        onSearchEvent = { searchViewModel.onEvent(it) },
        onFavoriteEvent = { favoriteViewModel.onEvent(it) },
        onNavigateToRecipe = onNavigateToRecipe
    )
}