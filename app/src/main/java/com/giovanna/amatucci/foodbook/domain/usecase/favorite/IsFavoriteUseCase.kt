package com.giovanna.amatucci.foodbook.domain.usecase.favorite

import com.giovanna.amatucci.foodbook.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

interface IsFavoriteUseCase {
    operator fun invoke(recipeId: String): Flow<Boolean>
}

class IsFavoriteUseCaseImpl(private val repository: FavoriteRepository) : IsFavoriteUseCase {
    override operator fun invoke(recipeId: String): Flow<Boolean> = repository.isFavorite(recipeId)

}