package com.giovanna.amatucci.foodbook.domain.usecase.favorite

import com.giovanna.amatucci.foodbook.domain.repository.FavoriteRepository

interface RemoveFavoriteUseCase {
    suspend operator fun invoke(recipe: String?)
}

class RemoveFavoriteUseCaseImpl(private val repository: FavoriteRepository) :
    RemoveFavoriteUseCase {
    override suspend fun invoke(recipe: String?) = repository.removeFavorite(recipe.toString())

}