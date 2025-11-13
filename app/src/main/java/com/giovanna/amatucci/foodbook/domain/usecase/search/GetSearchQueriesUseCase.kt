package com.giovanna.amatucci.foodbook.domain.usecase.search

import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository

interface GetSearchQueriesUseCase {
    suspend operator fun invoke(): List<String>
}

class GetSearchQueriesUseCaseImpl(private val repository: RecipeRepository) :
    GetSearchQueriesUseCase {
    override suspend fun invoke(): List<String> = repository.getSearchQueries()
}