package com.giovanna.amatucci.foodbook.presentation.details.content

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsEvent
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsUiState
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsTopBar(
    onNavigateBack: () -> Unit,
    onEvent: (DetailsEvent) -> Unit,
    state: DetailsUiState,
    modifier: Modifier = Modifier,
) {
    val isFavorite = state.isFavorite == true
    val (favoriteIcon, favoriteTint) = if (isFavorite) {
        Icons.Filled.Favorite to MaterialTheme.colorScheme.primary
    } else {
        Icons.Outlined.FavoriteBorder to MaterialTheme.colorScheme.onSurfaceVariant
    }
    Row(
        modifier = modifier, verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onNavigateBack) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.common_button_back),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = { onEvent(DetailsEvent.ToggleFavorite) }) {
            Icon(
                contentDescription = stringResource(R.string.favorites_description),
                imageVector = favoriteIcon,
                tint = favoriteTint
            )
        }
    }
}
