package com.giovanna.amatucci.foodbook.presentation.authentication.viewmodel.state
sealed interface AuthEvent {
    data object RequestToken : AuthEvent
    data object NavigationCompleted : AuthEvent
}