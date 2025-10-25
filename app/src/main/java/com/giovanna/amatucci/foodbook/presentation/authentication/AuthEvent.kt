package com.giovanna.amatucci.foodbook.presentation.authentication

sealed class AuthEvent {
    data object OnTokenRequest : AuthEvent()
    data object OnNavigationHandled : AuthEvent()
}