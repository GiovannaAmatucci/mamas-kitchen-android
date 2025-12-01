package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state

import com.giovanna.amatucci.foodbook.util.constants.UiText

/**
 * Represents the various operational statuses of the authentication process.
 */
sealed interface AuthStatus {
    /**
     * Indicates that a token check or fetch is currently in progress.
     */
    data object Loading : AuthStatus

    /**
     * Indicates that a valid token exists or was successfully fetched.
     */
    data object Success : AuthStatus

    /**
     * Indicates that the authentication process failed (e.g., network error).
     */
    data object Error : AuthStatus
}

/**
 * Represents the UI state for the Authentication screen.
 *
 * @param status The current operational status (Loading, Success, Error).
 * @param navigateToHome A one-time event flag. If true, the UI should navigate to the Main Screen.
 * @param error An optional error message wrapper (String or Resource ID) to display when status is Error.
 */
data class AuthState(
    val status: AuthStatus = AuthStatus.Loading,
    val navigateToHome: Boolean = false,
    val error: UiText? = null
)