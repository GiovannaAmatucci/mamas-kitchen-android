package com.giovanna.amatucci.foodbook.presentation.favorites.content

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.components.AppSearchBar
import com.giovanna.amatucci.foodbook.presentation.components.FavoriteLeadingIcon
import com.giovanna.amatucci.foodbook.presentation.components.FavoriteTrailingIcon
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoriteEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar(
    query: String,
    onEvent: (FavoriteEvent) -> Unit,
) {
    AppSearchBar(
        query = query,
        onQueryChange = { onEvent(FavoriteEvent.UpdateSearchQuery(it)) },
        onSearch = { onEvent(FavoriteEvent.SubmitSearch(it)) },
        isActive = true,
        expandable = false,
        placeholder = R.string.favorites_search_placeholder,
        trailingIcon = {
            FavoriteTrailingIcon(
                onDeleteAllClick = { onEvent(FavoriteEvent.ShowDeleteAllConfirmation) })
        },
        leadingIcon = { FavoriteLeadingIcon() })
}

