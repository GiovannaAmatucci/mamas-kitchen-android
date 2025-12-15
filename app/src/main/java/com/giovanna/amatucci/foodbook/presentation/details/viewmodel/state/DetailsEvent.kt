package com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state

sealed interface DetailsEvent {
    data object ToggleFavorite : DetailsEvent
    data object RetryConnection : DetailsEvent
}