package com.giovanna.amatucci.foodbook.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.usecase.favorite.GetFavoritesUseCase
import kotlinx.coroutines.flow.Flow


class FavoritesViewModel(
    getFavoritesUseCase: GetFavoritesUseCase
) : ViewModel() {

    val favoriteRecipes: Flow<PagingData<RecipeItem>> =
        getFavoritesUseCase()
            .cachedIn(viewModelScope)
}