package com.giovanna.amatucci.foodbook.presentation.details.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.giovanna.amatucci.foodbook.R
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.usecase.details.GetRecipeDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.AddFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesDetailsUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.IsFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.RemoveFavoritesUseCase
import com.giovanna.amatucci.foodbook.presentation.ScreenStatus
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsEvent
import com.giovanna.amatucci.foodbook.presentation.details.viewmodel.state.DetailsUiState
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.UiText
import com.giovanna.amatucci.foodbook.util.constants.VIEWMODEL.ARG_RECIPE_ID
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

    private val recipeId: String? = savedStateHandle.get<String>(ARG_RECIPE_ID)

    init {
        validateAndLoad(id = recipeId.toString())
    }

    fun onEvent(event: DetailsEvent) {
        when (event) {
            is DetailsEvent.ToggleFavorite -> toggleFavorite()
            is DetailsEvent.RetryConnection -> validateAndLoad(id = recipeId.toString())
            is DetailsEvent.OnImageChange -> {
                _uiState.update { it.copy(currentImageUrl = event.url) }
            }
        }
    }


    private fun validateAndLoad(id: String) {
        if (id.isBlank()) {
            _uiState.update {
                it.copy(
                    status = ScreenStatus.Error(UiText.StringResource(R.string.details_error_invalid_id))
                )
            }
            return
        }
        loadRecipeData(id)
        observeFavoriteStatus(id)
    }

    private fun loadRecipeData(id: String) = viewModelScope.launch {
        _uiState.update { it.copy(status = ScreenStatus.Loading) }
        val localRecipe = runCatching { getFavoritesDetailsUseCase(id) }.getOrNull()

        if (localRecipe != null) {
            onRecipeLoaded(localRecipe)
            return@launch
        }
        fetchRemoteDetails(id)
    }

    private suspend fun fetchRemoteDetails(id: String) {
        getRecipeDetailsUseCase(id).let { result ->
            when (result) {
                is ResultWrapper.Success -> onRecipeLoaded(result.data)
                is ResultWrapper.Error -> onError(UiText.DynamicString(result.message))
                is ResultWrapper.Exception -> onError(UiText.StringResource(R.string.details_error_api_failure))
            }
        }
    }

    private fun onRecipeLoaded(recipe: RecipeDetails) {
        _uiState.update {
            it.copy(
                status = ScreenStatus.Success,
                recipe = recipe,
                error = null,
                currentImageUrl = recipe.imageUrls?.firstOrNull()
            )
        }
    }

    private fun onError(message: UiText) {
        _uiState.update {
            it.copy(status = ScreenStatus.Error(message))
        }
    }

    private fun observeFavoriteStatus(id: String) {
        isFavoritesUseCase(id).onEach { isFavorite ->
            _uiState.update { it.copy(isFavorite = isFavorite) }
        }.launchIn(viewModelScope)
    }

    private fun toggleFavorite() = viewModelScope.launch {
        val currentState = _uiState.value
        val recipe = currentState.recipe ?: return@launch
        val validId = recipe.id.takeIf { !it.isNullOrBlank() } ?: return@launch
        runCatching {
            if (currentState.isFavorite == true) {
                removeFavoritesUseCase(validId)
            } else {
                addFavoritesUseCase(recipe)
            }
        }.onFailure { e ->
            _uiState.update { it.copy(error = UiText.DynamicString(e.message.toString())) }
        }
    }
}