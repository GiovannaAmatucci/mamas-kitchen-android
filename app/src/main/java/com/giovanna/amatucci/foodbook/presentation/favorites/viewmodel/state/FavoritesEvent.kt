package com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state

/** Represents all possible UI events/actions for the Favorites screen. */
sealed interface FavoritesEvent {
    /**◦Triggered when the user types in the search bar.
     * @param query The current text input. */
    data class UpdateSearchQuery(val query: String) : FavoritesEvent

    /**◦Triggered when the user clicks the "Delete All" button.◦Should display a confirmation dialog. */
    data object ShowDeleteAllConfirmation : FavoritesEvent

    /**◦Triggered when the user cancels the "Delete All" dialog. */
    data object DismissDeleteAllConfirmation : FavoritesEvent

    /**◦Triggered when the user confirms the "Delete All" dialog.◦Effectively wipes the database. */
    data object ConfirmDeleteAll : FavoritesEvent

    /**◦Triggered when the user presses the search action on the keyboard.
     * @param query The text to search for. */
    data class SubmitSearch(val query: String) : FavoritesEvent

    /**◦Triggered when the user clears the search query (e.g., clicking the 'X' icon). */
    data object ClearSearchQuery : FavoritesEvent
}