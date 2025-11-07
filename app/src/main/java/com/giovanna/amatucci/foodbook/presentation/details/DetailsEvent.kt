package com.giovanna.amatucci.foodbook.presentation.details

sealed interface DetailsEvent {
    data object ToggleFavorite : DetailsEvent
}