package com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.util.constants.UiText

/** Represents the loading status of the recipe details content. */
sealed interface DetailsStatus {
    data object Loading : DetailsStatus
    data object Success : DetailsStatus
    data object Error : DetailsStatus
}

/**•Represents the UI state for the Recipe Details screen.
 * @param status The current status of the data loading process (Loading, Success, Error).
 * @param recipe The loaded recipe details domain object. Null if loading or error.
 * @param error An optional error message to display if the status is Error.
 * @param isFavorite Indicates if the current recipe is saved in the local database. Null if status is loading.
 * @param isRefreshing Indicates if a background refresh is happening (optional usage for pull-to-refresh). */
data class DetailsUiState(
    val status: DetailsStatus = DetailsStatus.Loading,
    val recipe: RecipeDetails? = null,
    val error: UiText? = null,
    val isFavorite: Boolean? = null,
    val isRefreshing: Boolean = false
)