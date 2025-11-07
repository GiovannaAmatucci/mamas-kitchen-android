package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository

interface SaveSearchQueryUseCase {
    suspend operator fun invoke(query: String)
}

class SaveSearchQueryUseCaseImpl(private val repository: RecipeRepository) :
    SaveSearchQueryUseCase {
    override suspend fun invoke(query: String) = repository.saveSearchQuery(query)
}