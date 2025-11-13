package com.giovanna.amatucci.foodbook.presentation.details

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit, viewModel: DetailsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(topBar = {
        DetailsTopBar(
            title = state.recipe?.name ?: stringResource(R.string.details_screen_title_default),
            onNavigateBack = onNavigateBack,
            onEvent = { viewModel.onEvent(it) },
            state = state
        )
    }) { paddingValues ->
        DetailsContent(
            modifier = Modifier.padding(paddingValues), status = state.status, recipe = state.recipe
        )
    }
}

