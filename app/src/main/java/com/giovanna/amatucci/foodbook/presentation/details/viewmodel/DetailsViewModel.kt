package com.giovanna.amatucci.foodbook.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.UiText
import com.giovanna.amatucci.foodbook.domain.usecase.details.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.AddFavoriteUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.IsFavoriteUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.RemoveFavoriteUseCase
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsEvent
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsStatus
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsUiState
import com.giovanna.amatucci.foodbook.presentation.navigation.DetailsScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val navArgs: DetailsScreen = savedStateHandle.toRoute()
        val recipeId = navArgs.recipeId

        if (recipeId.isNotBlank()) {
            getRecipeDetails(recipeId)
            observeFavoriteStatus(recipeId)
        } else {
            _uiState.update {
                it.copy(
                    status = DetailsStatus.Error,
                    error = UiText.StringResource(R.string.details_error_invalid_id)
                )
            }
        }
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            DetailsEvent.ToggleFavorite -> toggleFavorite()
        }
    }
    private fun getRecipeDetails(recipeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(status = DetailsStatus.Loading) }
            getRecipeDetailsUseCase(recipeId).let { result ->
                when(result) {
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
    private fun observeFavoriteStatus(recipeId: String) {
        isFavoriteUseCase(recipeId).onEach { isFavorite ->
            _uiState.update { it.copy(isFavorite = isFavorite) }
        }.launchIn(viewModelScope)
    }

    private fun toggleFavorite() = viewModelScope.launch {
        val currentRecipe = _uiState.value.recipe ?: return@launch
        val recipeId = currentRecipe.id
        if (recipeId.isNullOrBlank()) {
            return@launch
        }
        if (_uiState.value.isFavorite == true) {
            removeFavoriteUseCase(recipeId)
        } else {
            addFavoriteUseCase(currentRecipe)
        }
    }
}