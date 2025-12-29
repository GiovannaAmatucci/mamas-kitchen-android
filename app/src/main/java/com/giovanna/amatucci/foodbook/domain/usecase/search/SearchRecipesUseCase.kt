package com.giovanna.amatucci.foodbook.domain.usecase.search

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

interface SearchRecipesUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<RecipeItem>>
}

@Factory(binds = [SearchRecipesUseCase::class])
class SearchRecipesUseCaseImpl(private val repository: RecipeRepository, ) : SearchRecipesUseCase {
    override suspend operator fun invoke(query: String): Flow<PagingData<RecipeItem>> {
        val trimmedQuery = query.trim()
        return repository.searchRecipesPaginated(trimmedQuery)
    }
}