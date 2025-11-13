package com.giovanna.amatucci.foodbook.domain.usecase.details

import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository

interface GetRecipeDetailsUseCase {
    suspend operator fun invoke(id: String): ResultWrapper<RecipeDetails>
}

class GetRecipeDetailsUseCaseImpl(private val repository: RecipeRepository) :
    GetRecipeDetailsUseCase {
    override suspend operator fun invoke(id: String): ResultWrapper<RecipeDetails> {
        return repository.getRecipeDetails(id)
    }
}

