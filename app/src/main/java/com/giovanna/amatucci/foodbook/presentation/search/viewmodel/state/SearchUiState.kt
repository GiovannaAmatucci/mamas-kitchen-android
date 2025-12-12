package com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.Category
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.presentation.ScreenState
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
data class SearchUiState(
    override val status: ScreenStatus = ScreenStatus.Loading,
    val searchQuery: String = "",
    val submittedQuery: String = "",
    val isActive: Boolean = false,
    val recipes: Flow<PagingData<RecipeItem>> = flowOf(PagingData.empty()),
    val searchHistory: List<String> = emptyList(),
    val shouldScrollToSearchTab: Boolean = false,
    val lastFavorites: List<RecipeItem>? = null,
    val categories: List<Category> = emptyList()
) : ScreenState