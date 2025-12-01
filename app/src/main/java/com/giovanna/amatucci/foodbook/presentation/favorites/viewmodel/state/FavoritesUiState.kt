package com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/** Represents the UI state for the Favorites screen.
 * @param searchQuery The current text used to filter the local favorites list.
 * @param recipes A Flow of PagingData containing the favorite recipes (filtered by query if present).
 * @param showConfirmDeleteAllDialog Boolean flag that controls the visibility of the "Delete All" confirmation dialog. */
data class FavoritesUiState(
    val searchQuery: String = "",
    val recipes: Flow<PagingData<RecipeItem>> = emptyFlow(),
    val showConfirmDeleteAllDialog: Boolean = false
)