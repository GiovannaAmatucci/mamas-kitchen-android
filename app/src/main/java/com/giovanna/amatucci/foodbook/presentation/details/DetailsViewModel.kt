package com.giovanna.amatucci.foodbook.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.usecase.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.presentation.navigation.DetailsScreen
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val navArgs: DetailsScreen = savedStateHandle.toRoute()
        val recipeId = navArgs.recipeId

        if (recipeId.isNotBlank()) {
            getRecipeDetails(recipeId)
        } else {
            _uiState.update {
                it.copy(
                    status = DetailsStatus.Error,
                    error = UiText.StringResource(R.string.details_error_invalid_id)
                )
            }
        }
    }

    private fun getRecipeDetails(recipeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(status = DetailsStatus.Loading) }
            when (val result = getRecipeDetailsUseCase(recipeId)) {
                is ResultWrapper.Success -> {
                    _uiState.update {
                        it.copy(status = DetailsStatus.Success, recipe = result.data)
                    }
                }

                is ResultWrapper.Error, is ResultWrapper.Exception -> {
                    _uiState.update {
                        it.copy(
                            status = DetailsStatus.Error,
                            error = UiText.StringResource(R.string.details_error_api_failure)
                        )
                    }
                }
            }
        }
    }
}