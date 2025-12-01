package com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state

/** Represents all possible UI events/actions for the Search screen. */
sealed interface SearchEvent {
    /**Triggered when the text in the search bar changes.
     * @param query The new text value. */
    data class UpdateSearchQuery(val query: String) : SearchEvent

    /**◦Triggered when the user submits a search (e.g., presses Enter/Search on keyboard).
     * @param query The final query to be executed against the API. */
    data class SubmitSearch(val query: String) : SearchEvent

    /**◦Triggered when a history item is clicked from the suggestion list.
     * @param query The historical query string selected. */
    data class RecentSearchClicked(val query: String) : SearchEvent

    /**◦Triggered when the search bar focus/active state changes.
     * @param active True if the search view is expanded/focused, false otherwise. */
    data class ActiveChanged(val active: Boolean) : SearchEvent

    /**◦Triggered to clear the current text in the search bar. */
    data object ClearSearchQuery : SearchEvent

    /**Triggered to delete the entire local search history. */
    data object ClearSearchHistory : SearchEvent
}