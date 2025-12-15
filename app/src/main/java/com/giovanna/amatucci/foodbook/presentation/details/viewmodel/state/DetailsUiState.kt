package com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.presentation.ScreenState
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.util.constants.UiText

data class DetailsUiState(
    override val status: ScreenStatus = ScreenStatus.Loading,
    val recipe: RecipeDetails? = null,
    val error: UiText? = null,
    val isFavorite: Boolean? = null,
    val isRefreshing: Boolean = false
) : ScreenState