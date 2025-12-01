package com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state

/**Represents all possible UI events/actions for the Details screen. */
sealed interface DetailsEvent {
    /**Triggered when the user clicks the favorite icon (Heart).
     * Adds or removes the current recipe from favorites. */
    data object ToggleFavorite : DetailsEvent

    /**Triggered when the user clicks 'Retry' on the error screen.
     * Attempts to fetch the recipe details again. */
    data object RetryConnection : DetailsEvent
}