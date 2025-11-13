package com.giovanna.amatucci.foodbook.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.DeleteAllFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.GetFavoritesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class FavoritesViewModel(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val deleteAllFavoritesUseCase: DeleteAllFavoritesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        val recipesFlow = _uiState.map { it.searchQuery }.distinctUntilChanged().debounce(300L)
            .flatMapLatest { query ->
                getFavoritesUseCase(query)
            }
            .cachedIn(viewModelScope)

        _uiState.update { it.copy(recipes = recipesFlow) }
    }

    fun onEvent(event: FavoriteEvent) {
        when (event) {
            is FavoriteEvent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }

            is FavoriteEvent.ShowDeleteAllConfirmation -> {
                _uiState.update { it.copy(showConfirmDeleteAllDialog = true) }
            }

            is FavoriteEvent.DismissDeleteAllConfirmation -> {
                _uiState.update { it.copy(showConfirmDeleteAllDialog = false) }
            }

            is FavoriteEvent.ConfirmDeleteAll -> {
                viewModelScope.launch {
                    deleteAllFavoritesUseCase()
                    _uiState.update { it.copy(showConfirmDeleteAllDialog = false) }
                }
            }

            is FavoriteEvent.SubmitSearch -> {
                _uiState.update { it.copy(searchQuery = event.query) }

            }

            is FavoriteEvent.ClearSearchQuery -> {
                _uiState.update { it.copy(searchQuery = "") }
            }
        }
    }
}