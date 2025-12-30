package com.giovanna.amatucci.foodbook.domain.repository

import androidx.paging.PagingData
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    suspend fun addFavorite(recipe: RecipeDetails)
    suspend fun getFavoriteDetails(recipeId: String): RecipeDetails?
    fun isFavorite(recipeId: String): Flow<Boolean>
    fun getFavorites(query: String): Flow<PagingData<RecipeItem>>
    suspend fun removeFavorite(recipeId: String)
    fun getLastFavorites(): Flow<List<RecipeItem>>
    suspend fun deleteAllFavorites()
}