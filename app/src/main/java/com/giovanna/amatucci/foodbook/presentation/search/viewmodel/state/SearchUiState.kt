package com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/** Represents the UI state for the Search screen.
 * @param searchQuery The current text typed into the search bar (mutable).
 * @param submittedQuery The query string that was actually submitted to the API (used to display results).
 * @param isActive Controls whether the Search Bar is expanded (focused) or collapsed.
 * @param recipes A Flow of PagingData containing the search results. Defaults to an empty flow.
 * @param searchHistory A list of recently searched query strings. */
data class SearchUiState(
    val searchQuery: String = "",
    val submittedQuery: String = "",
    val isActive: Boolean = false,
    val recipes: Flow<PagingData<RecipeItem>> = flowOf(PagingData.empty()),
    val searchHistory: List<String> = emptyList()
)