package com.giovanna.amatucci.foodbook.domain.repository

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun searchRecipesPaginated(
        query: String, recipeTypes: List<String>? = null
    ): Flow<PagingData<RecipeItem>>

    suspend fun getRecipeDetails(recipeId: String): ResultWrapper<RecipeDetails>
}

