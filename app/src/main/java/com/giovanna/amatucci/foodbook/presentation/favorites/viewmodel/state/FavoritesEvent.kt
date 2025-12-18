package com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state

sealed interface FavoritesEvent {
    data class UpdateSearchQuery(val query: String) : FavoritesEvent
    data object ShowDeleteAllConfirmation : FavoritesEvent
    data object DismissDeleteAllConfirmation : FavoritesEvent
    data object ConfirmDeleteAll : FavoritesEvent
    data class SubmitSearch(val query: String) : FavoritesEvent
    data object ClearSearchQuery : FavoritesEvent
}