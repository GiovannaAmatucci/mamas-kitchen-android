package com.giovanna.amatucci.foodbook.presentation.details.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.presentation.components.common.BlurredBackground
import com.giovanna.amatucci.foodbook.presentation.components.common.MessageComponent
import com.giovanna.amatucci.foodbook.presentation.components.recipe.RecipeDetailsList
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.DetailsViewModel
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsEvent
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsUiState
import com.giovanna.amatucci.foodbook.ui.theme.rememberScrimColor
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsRoute(
    onNavigateBack: () -> Unit,
    onSearchCategory: (String) -> Unit,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    DetailsScreen(
        state = state,
        onEvent = { onEvent -> viewModel.onEvent(onEvent) },
        onNavigateBack = onNavigateBack,
        onSearchCategory = onSearchCategory
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreen(
    state: DetailsUiState,
    onEvent: (DetailsEvent) -> Unit,
    onNavigateBack: () -> Unit,
    onSearchCategory: (String) -> Unit
) {
    state.apply {
        BlurredBackground(imageUrl = currentImageUrl, modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                DetailsScreenContent(
                    modifier = Modifier,
                    status = status,
                    recipe = recipe,
                    onImageDisplayed = { imageUrl ->
                        imageUrl?.let { onEvent(DetailsEvent.OnImageChange(imageUrl)) }
                    },
                    onCategoryClick = onSearchCategory
                )
                DetailsTopBar(
                    onNavigateBack = onNavigateBack,
                    onEvent = onEvent,
                    state = state,
                    modifier = Modifier.statusBarsPadding()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailsScreenContent(
    modifier: Modifier = Modifier,
    status: ScreenStatus,
    recipe: RecipeDetails?,
    onImageDisplayed: (String?) -> Unit = {},
    onCategoryClick: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(rememberScrimColor()),
        contentAlignment = Alignment.Center
    ) {
        when (status) {
            is ScreenStatus.Loading -> {
                DetailsScreenShimmer()
            }

            is ScreenStatus.Error -> MessageComponent(
                message = stringResource(R.string.details_error_message_loading_failed)
            )

            is ScreenStatus.Success -> {
                recipe?.let { details ->
                    RecipeDetailsList(
                        recipe = details,
                        modifier = Modifier.fillMaxSize(),
                        onImageDisplayed = onImageDisplayed,
                        onCategoryClick = onCategoryClick
                    )
                }
            }
        }
    }
}