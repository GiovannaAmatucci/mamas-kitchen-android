package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state

import com.giovanna.amatucci.foodbook.util.constants.UiText

sealed interface AuthStatus {
    data object Loading : AuthStatus
    data object Success : AuthStatus
    data object Error : AuthStatus
}

data class AuthState(
    val status: AuthStatus = AuthStatus.Loading,
    val navigateToHome: Boolean = false,
    val error: UiText? = null
)