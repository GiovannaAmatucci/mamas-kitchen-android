package com.giovanna.amatucci.foodbook.domain.usecase.favorite

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.FavoriteRepository

interface AddFavoriteUseCase {
    suspend operator fun invoke(
        recipe: RecipeDetails
    )
}

class AddFavoriteUseCaseImpl(private val repository: FavoriteRepository) : AddFavoriteUseCase {
    override suspend fun invoke(recipe: RecipeDetails) = repository.addFavorite(recipe)

}

