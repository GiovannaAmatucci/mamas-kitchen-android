package com.giovanna.amatucci.foodbook.domain.usecase.search

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

interface SearchRecipesUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<RecipeItem>>
}

class SearchRecipesUseCaseImpl(private val repository: RecipeRepository, ) : SearchRecipesUseCase {
    override suspend operator fun invoke(query: String): Flow<PagingData<RecipeItem>> {
        val trimmedQuery = query.trim()
        return repository.searchRecipesPaginated(trimmedQuery)
    }
}