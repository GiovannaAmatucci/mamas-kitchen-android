package com.giovanna.amatucci.foodbook.domain.usecase

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import kotlinx.coroutines.flow.Flow


interface SearchRecipesUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<RecipeItem>>
}

class SearchRecipesUseCaseImpl(private val repository: RecipeRepository) : SearchRecipesUseCase {
    override suspend operator fun invoke(query: String): Flow<PagingData<RecipeItem>> {
        val trimmedQuery = query.trim()
        return repository.searchRecipesPaginated(trimmedQuery)
    }
}
