package com.giovanna.amatucci.foodbook.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.giovanna.amatucci.foodbook.domain.usecase.search.ClearSearchHistoryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.GetSearchQueriesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SaveSearchQueryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SearchRecipesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
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

    private val _searchHistory = MutableStateFlow<List<String>>(emptyList())
    val searchHistory: StateFlow<List<String>> = _searchHistory.asStateFlow()
    init {
        getSearchHistory()
    }

    fun searchRecipe(query: String) {
        viewModelScope.launch {
            saveSearchQueryUseCase(query)
            getSearchHistory()

            val newProductsFlow = searchRecipesUseCase(query).cachedIn(viewModelScope)

            _uiState.update {
                it.copy(
                    recipes = newProductsFlow, isActive = false
                )
            }
        }
    }

    fun getSearchHistory() {
        viewModelScope.launch {
            val history = getSearchQueriesUseCase()
            _searchHistory.value = history
        }
    }


    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }

            is SearchEvent.SubmitSearch -> {
                val query = event.query
                _uiState.update { it.copy(submittedQuery = query) }

                if (query.isBlank()) {
                    _uiState.update {
                        it.copy(
                            recipes = flowOf(PagingData.empty())
                        )
                    }
                } else {
                    searchRecipe(query)
                }
            }

            is SearchEvent.RecentSearchClicked -> {
                val query = event.query
                _uiState.update {
                    it.copy(
                        searchQuery = query,
                        submittedQuery = query,
                        isActive = false
                    )
                }
                searchRecipe(query)
            }

            is SearchEvent.ClearSearchQuery -> {
                _uiState.update {
                    it.copy(
                        searchQuery = ""
                    )
                }
            }

            is SearchEvent.ClearSearchHistory -> {
                viewModelScope.launch {
                    clearSearchHistoryUseCase()
                    getSearchHistory()
                }
            }

            is SearchEvent.ActiveChanged -> {
                _uiState.update { it.copy(isActive = event.active) }
                if (event.active) {
                    getSearchHistory()
                }
            }
        }
    }
}