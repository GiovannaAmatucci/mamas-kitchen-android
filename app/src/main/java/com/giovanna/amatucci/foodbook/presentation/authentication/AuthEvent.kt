package com.giovanna.amatucci.foodbook.presentation.authentication

sealed interface AuthEvent {
    data object RequestToken : AuthEvent
    data object NavigationCompleted : AuthEvent
}