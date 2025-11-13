package com.giovanna.amatucci.foodbook.presentation.details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
    title: String,
    onNavigateBack: () -> Unit,
    onEvent: (DetailsEvent) -> Unit,
    state: DetailsUiState,
) {
    TopAppBar(title = {
        Text(title)
    }, navigationIcon = {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.common_button_back)
            )
        }
    }, actions = {
        IconButton(onClick = { onEvent(DetailsEvent.ToggleFavorite) }) {
            Icon(
                contentDescription = stringResource(R.string.favorites_description),
                imageVector = if (state.isFavorite == true) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                tint = if (state.isFavorite == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )

        }
    })
}