package com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class FavoritesUiState(
    val searchQuery: String = "",
    val recipes: Flow<PagingData<RecipeItem>> = emptyFlow(),
    val showConfirmDeleteAllDialog: Boolean = false
)