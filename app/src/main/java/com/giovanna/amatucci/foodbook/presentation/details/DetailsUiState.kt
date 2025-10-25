package com.giovanna.amatucci.foodbook.presentation.details

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails

sealed interface DetailsUiState {
    data object Loading : DetailsUiState
    data class Success(val recipeDetails: RecipeDetails) : DetailsUiState
    data class Error(val message: String? = null) : DetailsUiState
}