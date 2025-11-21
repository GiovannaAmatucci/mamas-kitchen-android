package com.giovanna.amatucci.foodbook.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.usecase.details.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.AddFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.IsFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.RemoveFavoritesUseCase
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsEvent
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsStatus
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsUiState
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.UiConstants
import com.giovanna.amatucci.foodbook.util.constants.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase,
    private val isFavoritesUseCase: IsFavoritesUseCase,
    private val getFavoritesDetailsUseCase: GetFavoritesDetailsUseCase,
    private val addFavoritesUseCase: AddFavoritesUseCase,
    private val removeFavoritesUseCase: RemoveFavoritesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    private val recipeId: String? =
        savedStateHandle.get<String>(UiConstants.DETAILS_VIEW_MODEL_RECIPE_ID)

    init {
        if (recipeId.isNullOrBlank()) {
            _uiState.update {
                it.copy(
                    status = DetailsStatus.Error,
                    error = UiText.StringResource(R.string.details_error_invalid_id)
                )
            }
        } else {
            getRecipeDetails(recipeId)
            observeFavoriteStatus(recipeId)
        }
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.ToggleFavorite -> toggleFavorite()
            is DetailsEvent.RetryConnection -> {
                recipeId?.let { id ->
                    getRecipeDetails(id)
                }
            }
        }
    }

    private fun getRecipeDetails(recipeId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(status = DetailsStatus.Loading) }
            val localRecipe = getFavoritesDetailsUseCase(recipeId)

            if (localRecipe != null) {
                _uiState.update {
                    it.copy(status = DetailsStatus.Success, recipe = localRecipe)
                }
            } else {
                getRecipeDetailsUseCase(recipeId).let { result ->
                    when (result) {
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
    }

    private fun observeFavoriteStatus(recipeId: String) {
        isFavoritesUseCase(recipeId).onEach { isFavorite ->
            _uiState.update { it.copy(isFavorite = isFavorite) }
        }.launchIn(viewModelScope)
    }

    private fun toggleFavorite() = viewModelScope.launch {
        val currentState = _uiState.value
        val recipe = currentState.recipe ?: return@launch
        val recipeId = recipe.id.takeIf { !it.isNullOrBlank() } ?: return@launch
        if (currentState.isFavorite == true) removeFavoritesUseCase(recipeId)
        else addFavoritesUseCase(recipe)
    }
}