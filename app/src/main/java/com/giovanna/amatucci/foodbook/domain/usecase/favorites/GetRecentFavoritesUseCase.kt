package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

interface GetRecentFavoritesUseCase {
    operator fun invoke(): Flow<List<RecipeItem>>
}
class GetRecentFavoritesUseCaseImpl(
    private val repository: FavoritesRepository
) : GetRecentFavoritesUseCase {
    override fun invoke(): Flow<List<RecipeItem>> = repository.getLastFavorites()
}