package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface SaveSearchQueryUseCase {
    suspend operator fun invoke(query: String)
}

class SaveSearchQueryUseCaseImpl(private val repository: RecipeRepository) :
    SaveSearchQueryUseCase {
    override suspend fun invoke(query: String) = withContext(Dispatchers.IO) {
        repository.saveSearchQuery(query)
    }
}