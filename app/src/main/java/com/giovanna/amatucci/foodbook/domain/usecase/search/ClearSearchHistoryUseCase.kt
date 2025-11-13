package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ClearSearchHistoryUseCase {
    suspend operator fun invoke()
}

class ClearSearchHistoryUseCaseImpl(
    private val repository: RecipeRepository
) : ClearSearchHistoryUseCase {
    override suspend fun invoke() = withContext(Dispatchers.IO) {
        repository.clearSearchHistory()
    }
}