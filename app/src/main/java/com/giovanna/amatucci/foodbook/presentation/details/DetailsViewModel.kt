package com.giovanna.amatucci.foodbook.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.foodbook.data.network.ApiResult
import com.giovanna.amatucci.foodbook.domain.usecase.GetRecipeDetailsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase, private val recipeId: Int
) : ViewModel() {
    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchRecipeDetails(recipeId)
    }

    fun fetchRecipeDetails(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading

            val result = getRecipeDetailsUseCase(id)

            when (result) {
                is ApiResult.Success -> {
                    _uiState.value = DetailsUiState.Success(result.data)
                }

                is ApiResult.Error -> {
                    val errorMessage = result.exception.message
                    _uiState.value = DetailsUiState.Error(errorMessage)
                }
            }
        }
    }
}