package com.giovanna.amatucci.foodbook.domain.repository

import com.giovanna.amatucci.foodbook.data.network.ApiResult
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeSummary

interface RecipeRepository {
    suspend fun searchRecipes(query: String): ApiResult<List<RecipeSummary>>
    suspend fun getRecipeDetails(id: Int): ApiResult<RecipeDetails>
}