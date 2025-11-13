package com.giovanna.amatucci.foodbook.presentation.favorites

sealed interface FavoriteEvent {
    data class UpdateSearchQuery(val query: String) : FavoriteEvent
    data object ShowDeleteAllConfirmation : FavoriteEvent
    data object DismissDeleteAllConfirmation : FavoriteEvent
    data object ConfirmDeleteAll : FavoriteEvent

    data class SubmitSearch(val query: String) : FavoriteEvent
    object ClearSearchQuery : FavoriteEvent
}