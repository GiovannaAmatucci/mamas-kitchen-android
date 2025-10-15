package com.giovanna.amatucci.foodbook.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.foodbook.data.network.ApiResult
import com.giovanna.amatucci.foodbook.domain.usecase.SearchRecipesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRecipesUseCase: SearchRecipesUseCase,
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _uiState = MutableStateFlow<SearchScreenUiState>(SearchScreenUiState.Idle)
    val uiState = _uiState.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun searchRecipes() {
        val query = _searchQuery.value
        if (query.isBlank()) return

        viewModelScope.launch {
            _uiState.value = SearchScreenUiState.Loading
            val result = searchRecipesUseCase(query)
            when (result) {
                is ApiResult.Success -> {
                    if (result.data.isEmpty()) {
                        _uiState.value = SearchScreenUiState.Empty
                    } else {
                        _uiState.value = SearchScreenUiState.Success(result.data)
                    }
                }

                is ApiResult.Error -> {
                    val errorMessage = result.exception.message
                    _uiState.value = SearchScreenUiState.Error(errorMessage)
                }
            }
        }
    }
}
