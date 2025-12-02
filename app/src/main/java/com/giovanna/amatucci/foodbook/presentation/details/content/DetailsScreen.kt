package com.giovanna.amatucci.foodbook.presentation.details.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.presentation.components.BlurredImageComposable
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.DetailsViewModel
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsEvent
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsUiState
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import org.koin.androidx.compose.koinViewModel
@Composable
fun DetailsRoute(
    onNavigateBack: () -> Unit, viewModel: DetailsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    DetailsScreen(
        state = state, onEvent = { viewModel.onEvent(it) }, onNavigateBack = onNavigateBack
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreen(
    state: DetailsUiState, onEvent: (DetailsEvent) -> Unit, onNavigateBack: () -> Unit
) {
    var currentMainImageUrl by remember(state.recipe) {
        mutableStateOf(state.recipe?.imageUrls?.firstOrNull())
    }

    BlurredImageComposable(
        imageUrl = currentMainImageUrl,
        blurRadius = Dimens.BlurRadius,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            DetailsContent(
                modifier = Modifier,
                status = state.status, recipe = state.recipe,
                onImageDisplayed = { imageUrl ->
                    currentMainImageUrl = imageUrl
                })

            DetailsTopBar(
                onNavigateBack = onNavigateBack, onEvent = onEvent,
                state = state,
                modifier = Modifier.statusBarsPadding()
            )
        }
    }
}

