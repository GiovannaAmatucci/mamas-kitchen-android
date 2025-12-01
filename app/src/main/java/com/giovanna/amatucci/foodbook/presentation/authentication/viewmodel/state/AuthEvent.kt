package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state

/** Represents all possible UI events/actions for the Authentication screen. */
sealed interface AuthEvent {
    /** Triggered manually (e.g., Retry button) to request a new access token from the API. */
    data object RequestToken : AuthEvent

    /** Triggered by the UI (LaunchedEffect) to notify the ViewModel that
     * the navigation to the Home screen has been performed, so the state can be reset. */
    data object NavigationCompleted : AuthEvent
}