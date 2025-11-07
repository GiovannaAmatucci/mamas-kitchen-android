package com.giovanna.amatucci.foodbook.domain.usecase.favorite

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

interface GetFavoritesUseCase {
    operator fun invoke(): Flow<PagingData<RecipeItem>>
}


class GetFavoritesUseCaseImpl(private val repository: FavoriteRepository) : GetFavoritesUseCase {
    override operator fun invoke(): Flow<PagingData<RecipeItem>> = repository.getFavorites()
}
