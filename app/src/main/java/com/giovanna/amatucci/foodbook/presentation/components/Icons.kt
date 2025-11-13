package com.giovanna.amatucci.foodbook.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R

@Composable
fun FavoriteTrailingIcon(onDeleteAllClick: () -> Unit) {
    IconButton(onClick = onDeleteAllClick) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.favorites_delete_all_description)
        )
    }
}

@Composable
fun FavoriteLeadingIcon(
    imageVector: ImageVector = Icons.Default.Search, contentDescription: String? = null
) {
    Icon(
        imageVector = imageVector, contentDescription = contentDescription
    )
}


@Composable
fun SearchTrailingIcon(query: String, onClearClick: () -> Unit) {
    if (query.isNotEmpty()) IconButton(onClick = { onClearClick() }) {
        Icon(
            Icons.Default.Close,
            contentDescription = stringResource(R.string.search_close_description)

        )
    }
}


@Composable
fun SearchLeadingIcon(
    isActive: Boolean,
    onActiveChange: (Boolean) -> Unit,
) {
    if (isActive) IconButton(onClick = { onActiveChange(false) }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.common_button_back)
        )
    } else {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = stringResource(R.string.search_screen_title)
        )
    }
}