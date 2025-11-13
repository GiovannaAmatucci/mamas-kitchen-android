package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state

import com.giovanna.amatucci.foodbook.di.util.constants.UiText
sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Authenticated(
        val navigateToHome: Boolean = false
    ) : AuthUiState

    data class AuthenticationFailed(val errorMessage: UiText) : AuthUiState
}
