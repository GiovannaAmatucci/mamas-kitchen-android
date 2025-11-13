package com.giovanna.amatucci.foodbook.presentation.favorites

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
import com.giovanna.amatucci.foodbook.presentation.components.SearchNavTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesTopBar(
    query: String,
    onEvent: (FavoriteEvent) -> Unit,
) {
    SearchNavTopAppBar(
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

@Composable
private fun FavoriteLeadingIcon(
    imageVector: ImageVector = Icons.Default.Search, contentDescription: String? = null
) {
    Icon(
        imageVector = imageVector, contentDescription = contentDescription
    )
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
