package com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.DeleteAllFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetFavoritesUseCase
import com.giovanna.amatucci.foodbook.domain.usecase.favorites.GetRecentFavoritesUseCase
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesEvent
import com.giovanna.amatucci.foodbook.presentation.favorites.viewmodel.state.FavoritesUiState
import com.giovanna.amatucci.foodbook.util.constants.VIEWMODEL.FAVORITE_DEBOUNCE
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
    private val deleteAllFavoritesUseCase: DeleteAllFavoritesUseCase,
    private val getRecentFavoritesUseCase: GetRecentFavoritesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        initializeSearchFlow()
        observeDatabaseCount()
    }

    fun onEvent(event: FavoritesEvent) {
        when (event) {
            is FavoritesEvent.UpdateSearchQuery -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }

            is FavoritesEvent.ShowDeleteAllConfirmation -> {
                _uiState.update { it.copy(showConfirmDeleteAllDialog = true) }
            }

            is FavoritesEvent.DismissDeleteAllConfirmation -> {
                _uiState.update { it.copy(showConfirmDeleteAllDialog = false) }
            }

            is FavoritesEvent.ConfirmDeleteAll -> {
                performDeleteAll()
            }

            is FavoritesEvent.SubmitSearch -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }

            is FavoritesEvent.ClearSearchQuery -> {
                _uiState.update { it.copy(searchQuery = "") }
            }
        }
    }

    private fun observeDatabaseCount() = viewModelScope.launch {
        getRecentFavoritesUseCase().collect { list ->
            _uiState.update { it.copy(hasAnyFavorite = list.isNotEmpty()) }
        }
    }
    private fun initializeSearchFlow() {
        val recipesFlow = _uiState.map { it.searchQuery }.distinctUntilChanged()
            .debounce(FAVORITE_DEBOUNCE).flatMapLatest { query ->
                getFavoritesUseCase(query)
            }.cachedIn(viewModelScope)

        _uiState.update { it.copy(recipes = recipesFlow) }
    }

    private fun performDeleteAll() = viewModelScope.launch {
        deleteAllFavoritesUseCase()
        _uiState.update { it.copy(showConfirmDeleteAllDialog = false) }
    }
}