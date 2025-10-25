package com.giovanna.amatucci.foodbook.domain.usecase

import com.giovanna.amatucci.foodbook.data.network.ApiResult
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository

interface GetRecipeDetailsUseCase {
    suspend operator fun invoke(id: Int): ApiResult<RecipeDetails>
}

class GetRecipeDetailsUseCaseImpl(private val repository: RecipeRepository) :
    GetRecipeDetailsUseCase {
    override suspend operator fun invoke(id: Int): ApiResult<RecipeDetails> {
        return repository.getRecipeDetails(id)
    }
}

