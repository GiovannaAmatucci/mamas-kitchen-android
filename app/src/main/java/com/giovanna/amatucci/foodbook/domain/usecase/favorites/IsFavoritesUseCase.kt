package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

interface IsFavoritesUseCase {
    operator fun invoke(recipeId: String): Flow<Boolean>
}
class IsFavoritesUseCaseImpl(private val repository: FavoritesRepository) : IsFavoritesUseCase {
    override operator fun invoke(recipeId: String): Flow<Boolean> = repository.isFavorite(recipeId)
}