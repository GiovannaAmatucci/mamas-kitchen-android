package com.giovanna.amatucci.foodbook.presentation.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.giovanna.amatucci.foodbook.domain.usecase.search.ClearSearchHistoryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.GetSearchQueriesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SaveSearchQueryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SearchRecipesUseCase
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchEvent
import com.giovanna.amatucci.foodbook.presentation.search.viewmodel.state.SearchUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase,
    private val getSearchQueriesUseCase: GetSearchQueriesUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        fetchSearchHistory()
    }
    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            is SearchEvent.SubmitSearch -> handleSearchSubmission(event.query)
            is SearchEvent.RecentSearchClicked -> {
                _uiState.update {
                    it.copy(
                        searchQuery = event.query,
                        submittedQuery = event.query,
                        isActive = false
                    )
                }
                performSearch(event.query)
            }

            is SearchEvent.ClearSearchQuery -> _uiState.update { it.copy(searchQuery = "") }
            is SearchEvent.ClearSearchHistory -> clearHistory()
            is SearchEvent.ActiveChanged -> {
                _uiState.update { it.copy(isActive = event.active) }
                if (event.active) fetchSearchHistory()
            }
        }
    }

    private fun handleSearchSubmission(query: String) {
        _uiState.update { it.copy(submittedQuery = query) }

        if (query.isBlank()) {
            _uiState.update {
                it.copy(submittedQuery = "", recipes = flowOf(PagingData.empty()))
            }
        } else {
            performSearch(query)
        }
    }

    private fun performSearch(query: String) {
        triggerSearchFlow(query)
        saveQueryToHistory(query)
    }

    private fun triggerSearchFlow(query: String) = viewModelScope.launch {
        val newPagingFlow = searchRecipesUseCase(query).cachedIn(viewModelScope)

        _uiState.update {
            it.copy(recipes = newPagingFlow, isActive = false)
        }
    }

    private fun saveQueryToHistory(query: String) = viewModelScope.launch {
        runCatching {
            saveSearchQueryUseCase(query)
            fetchSearchHistory()
        }
    }

    private fun fetchSearchHistory() {
        viewModelScope.launch {
            runCatching { getSearchQueriesUseCase() }
                .onSuccess { history ->
                    _uiState.update { it.copy(searchHistory = history) }
                }
        }
    }

    private fun clearHistory() {
        viewModelScope.launch {
            runCatching {
                clearSearchHistoryUseCase()
                fetchSearchHistory()
            }
        }
    }
}