package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import org.koin.core.annotation.Factory

interface RemoveFavoritesUseCase {
    suspend operator fun invoke(recipe: String?)
}
@Factory(binds = [RemoveFavoritesUseCase::class])
class RemoveFavoritesUseCaseImpl(private val repository: FavoritesRepository) : RemoveFavoritesUseCase {
    override suspend fun invoke(recipe: String?) = repository.removeFavorite(recipe.toString())
}