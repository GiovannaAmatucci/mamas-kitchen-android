package com.giovanna.amatucci.foodbook.presentation.search

import com.giovanna.amatucci.foodbook.domain.model.RecipeSummary

sealed interface SearchScreenUiState {
    data object Idle : SearchScreenUiState

    data object Loading : SearchScreenUiState

    data class Success(val recipes: List<RecipeSummary>) : SearchScreenUiState

    data object Empty : SearchScreenUiState

    data class Error(val message: String? = null) : SearchScreenUiState
}