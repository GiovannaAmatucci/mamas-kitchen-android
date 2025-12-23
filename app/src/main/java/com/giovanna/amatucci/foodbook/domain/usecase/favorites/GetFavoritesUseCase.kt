package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

interface GetFavoritesUseCase {
    operator fun invoke(query: String): Flow<PagingData<RecipeItem>>
}
class GetFavoritesUseCaseImpl(private val repository: FavoritesRepository) : GetFavoritesUseCase {
    override operator fun invoke(query: String): Flow<PagingData<RecipeItem>> =
        repository.getFavorites(query)
}
