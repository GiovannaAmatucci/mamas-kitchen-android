package com.giovanna.amatucci.foodbook.presentation.search

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class SearchUiState(
    val searchQuery: String = "", val recipes: Flow<PagingData<RecipeItem>> = emptyFlow()
)