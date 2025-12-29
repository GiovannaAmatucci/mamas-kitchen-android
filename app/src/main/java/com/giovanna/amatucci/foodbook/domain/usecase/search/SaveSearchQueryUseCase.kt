package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository
import org.koin.core.annotation.Factory

interface SaveSearchQueryUseCase {
    suspend operator fun invoke(query: String)
}
@Factory(binds = [SaveSearchQueryUseCase::class])
class SaveSearchQueryUseCaseImpl(private val repository: SearchRepository) : SaveSearchQueryUseCase {
    override suspend fun invoke(query: String) {
        if(query.isBlank()) return
        repository.saveSearchQuery(query.trim())
    }
}