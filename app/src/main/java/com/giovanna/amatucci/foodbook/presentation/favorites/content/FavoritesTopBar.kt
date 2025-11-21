package com.giovanna.amatucci.foodbook.presentation.favorites.content

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.AppSearchBarComposable
import com.giovanna.amatucci.foodbook.presentation.components.FavoriteLeadingIcon
import com.giovanna.amatucci.foodbook.presentation.components.FavoriteTrailingIcon
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesEvent
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar(
    state: FavoritesUiState,
    onEvent: (FavoritesEvent) -> Unit,
) {
    AppSearchBarComposable(
        query = state.searchQuery,
        onQueryChange = { onEvent(FavoritesEvent.UpdateSearchQuery(it)) },
        onSearch = { onEvent(FavoritesEvent.SubmitSearch(it)) },
        isActive = true,
        expandable = false,
        placeholder = R.string.favorites_search_placeholder,
        trailingIcon = {
            FavoriteTrailingIcon(
                onDeleteAllClick = { onEvent(FavoritesEvent.ShowDeleteAllConfirmation) })
        },
        leadingIcon = { FavoriteLeadingIcon() })
}

