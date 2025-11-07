package com.giovanna.amatucci.foodbook.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.giovanna.amatucci.foodbook.domain.usecase.search.SaveSearchQueryUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.search.SearchRecipesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update


@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModel(
    private val searchRecipesUseCase: SearchRecipesUseCase,
    private val saveSearchQueryUseCase: SaveSearchQueryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        val recipesFlow = _uiState
            .debounce(500L)
            .distinctUntilChanged { old, new -> old.searchQuery == new.searchQuery }
            .onEach { state ->
                if (state.searchQuery.length > 2) {
                    saveSearchQueryUseCase(state.searchQuery)
                }
            }
            .flatMapLatest { state ->
                if (state.searchQuery.length > 2) {
                    searchRecipesUseCase(state.searchQuery)
                } else {
                    emptyFlow()
                }
            }
            .cachedIn(viewModelScope)

        _uiState.update { it.copy(recipes = recipesFlow) }
    }

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearchQueryChange -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
        }
    }
}