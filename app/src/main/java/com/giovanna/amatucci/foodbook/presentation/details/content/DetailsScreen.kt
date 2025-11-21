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
import com.giovanna.amatucci.foodbook.ui.theme.Dimens
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    onNavigateBack: () -> Unit, viewModel: DetailsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

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
                status = state.status,
                recipe = state.recipe, onEvent = { viewModel.onEvent(it) },
                onImageDisplayed = { imageUrl ->
                    currentMainImageUrl = imageUrl
                })

            DetailsTopBar(
                onNavigateBack = onNavigateBack,
                onEvent = { viewModel.onEvent(it) },
                state = state,
                modifier = Modifier.statusBarsPadding()
            )
        }
    }
}

