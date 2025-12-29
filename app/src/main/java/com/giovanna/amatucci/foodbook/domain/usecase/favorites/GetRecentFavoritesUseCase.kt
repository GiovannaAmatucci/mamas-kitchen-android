package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface GetRecentFavoritesUseCase {
    operator fun invoke(): Flow<List<RecipeItem>>
}
@Factory(binds = [GetRecentFavoritesUseCase::class])
class GetRecentFavoritesUseCaseImpl(private val repository: FavoritesRepository) : GetRecentFavoritesUseCase {
    override fun invoke(): Flow<List<RecipeItem>> = repository.getLastFavorites()
}