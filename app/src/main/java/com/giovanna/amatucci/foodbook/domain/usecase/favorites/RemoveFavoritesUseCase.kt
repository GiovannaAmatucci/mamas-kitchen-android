package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository

interface RemoveFavoritesUseCase {
    suspend operator fun invoke(recipe: String?)
}
class RemoveFavoritesUseCaseImpl(private val repository: FavoritesRepository) :
    RemoveFavoritesUseCase {
    override suspend fun invoke(recipe: String?) = repository.removeFavorite(recipe.toString())
}