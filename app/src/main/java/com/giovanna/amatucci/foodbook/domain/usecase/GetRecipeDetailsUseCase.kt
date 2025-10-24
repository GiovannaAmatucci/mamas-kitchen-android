package com.giovanna.amatucci.foodbook.domain.usecase

import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.util.ResultWrapper

interface GetRecipeDetailsUseCase {
    suspend operator fun invoke(id: String): ResultWrapper<RecipeDetails>
}

class GetRecipeDetailsUseCaseImpl(private val repository: RecipeRepository) :
    GetRecipeDetailsUseCase {
    override suspend operator fun invoke(id: String): ResultWrapper<RecipeDetails> {
        return repository.getRecipeDetails(id)
    }
}

