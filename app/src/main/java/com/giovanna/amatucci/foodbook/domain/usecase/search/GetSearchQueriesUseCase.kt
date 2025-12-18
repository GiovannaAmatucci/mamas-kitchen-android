package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository

interface GetSearchQueriesUseCase {
    suspend operator fun invoke(): List<String>
}


class GetSearchQueriesUseCaseImpl(private val repository: SearchRepository) :
    GetSearchQueriesUseCase {
    private companion object {
        const val HISTORY_LIMIT = 5
    }

    override suspend fun invoke(): List<String> = repository.getSearchQueries().take(HISTORY_LIMIT)
}