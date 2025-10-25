package com.giovanna.amatucci.foodbook.domain.usecase

import com.giovanna.amatucci.foodbook.data.network.ApiResult
import com.giovanna.amatucci.foodbook.domain.model.RecipeSummary
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository


interface SearchRecipesUseCase {
    suspend operator fun invoke(query: String): ApiResult<List<RecipeSummary>>
}

class SearchRecipesUseCaseImpl(private val repository: RecipeRepository) : SearchRecipesUseCase {
    override suspend operator fun invoke(query: String): ApiResult<List<RecipeSummary>> {
        return repository.searchRecipes(query)
    }
}
