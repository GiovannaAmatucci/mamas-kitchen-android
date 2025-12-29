package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository
import org.koin.core.annotation.Factory

interface ClearSearchHistoryUseCase {
    suspend operator fun invoke()
}
@Factory(binds = [ClearSearchHistoryUseCase::class])
class ClearSearchHistoryUseCaseImpl(private val repository: SearchRepository) : ClearSearchHistoryUseCase {
    override suspend fun invoke() = repository.clearSearchHistory()
}
