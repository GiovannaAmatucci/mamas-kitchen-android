package com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class SearchUiState(
    val searchQuery: String = "",
    val submittedQuery: String = "",
    val isActive: Boolean = false,
    val recipes: Flow<PagingData<RecipeItem>> = flowOf(PagingData.empty())
)