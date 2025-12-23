package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository

interface AddFavoritesUseCase {
    suspend operator fun invoke(
        recipe: RecipeDetails
    )
}
class AddFavoritesUseCaseImpl(private val repository: FavoritesRepository) : AddFavoritesUseCase {
    override suspend fun invoke(recipe: RecipeDetails) = repository.addFavorite(recipe)

}

