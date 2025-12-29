package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import org.koin.core.annotation.Factory

interface GetFavoritesDetailsUseCase {
    suspend operator fun invoke(recipeId: String): RecipeDetails?
}

@Factory(binds = [GetFavoritesDetailsUseCase::class])
class GetFavoritesDetailsUseCaseImpl(private val repository: FavoritesRepository) : GetFavoritesDetailsUseCase {
    override suspend fun invoke(recipeId: String): RecipeDetails? = repository.getFavoriteDetails(recipeId)
}