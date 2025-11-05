package com.giovanna.amatucci.foodbook.presentation.details

import com.giovanna.amatucci.foodbook.di.util.constants.UiText
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails

data class DetailUiState(
    val status: DetailsStatus = DetailsStatus.Loading,
    val recipe: RecipeDetails? = null,
    val error: UiText? = null
)

