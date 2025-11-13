package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ClearSearchHistoryUseCase {
    suspend operator fun invoke()
}

class ClearSearchHistoryUseCaseImpl(
    private val repository: SearchRepository
) : ClearSearchHistoryUseCase {
    override suspend fun invoke() = withContext(Dispatchers.IO) {
        repository.clearSearchHistory()
    }
}