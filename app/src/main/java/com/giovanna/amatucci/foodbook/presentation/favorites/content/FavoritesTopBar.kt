package com.giovanna.amatucci.foodbook.presentation.favorites.content

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.search.SearchBarWrapper
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesEvent
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar(
    state: FavoritesUiState,
    onEvent: (FavoritesEvent) -> Unit,
) {
    SearchBarWrapper(
        query = state.searchQuery,
        onQueryChange = { onQueryChange -> onEvent(FavoritesEvent.UpdateSearchQuery(onQueryChange)) },
        onSearch = { onSearch -> onEvent(FavoritesEvent.SubmitSearch(onSearch)) },
        isActive = true,
        expandable = false,
        placeholder = R.string.favorites_search_placeholder,
        trailingIcon = {
            FavoriteTrailingIcon(
                onDeleteAllClick = { onEvent(FavoritesEvent.ShowDeleteAllConfirmation) })
        },
        leadingIcon = { FavoriteLeadingIcon() })
}

@Composable
private fun FavoriteTrailingIcon(onDeleteAllClick: () -> Unit) {
    IconButton(onClick = onDeleteAllClick) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.favorites_delete_all_description)
        )
    }
}

@Composable
private fun FavoriteLeadingIcon(
    imageVector: ImageVector = Icons.Default.Search,
    contentDescription: String? = null
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription
    )
}

