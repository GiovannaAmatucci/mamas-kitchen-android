package com.giovanna.amatucci.foodbook.data.local.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giovanna.amatucci.foodbook.data.local.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertFavorite(recipe: FavoriteEntity)

    @Query("DELETE FROM favorite_recipes WHERE recipe_id = :recipeId")
    suspend fun deleteFavorite(recipeId: String?)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_recipes WHERE recipe_id = :recipeId LIMIT 1)")
    fun isFavorite(recipeId: Long): Flow<Boolean>

    @Query("SELECT * FROM favorite_recipes WHERE name LIKE :query ORDER BY date_favorites DESC")
    fun getAllFavoritesPaged(query: String): PagingSource<Int, FavoriteEntity>

    @Query("DELETE FROM favorite_recipes")
    suspend fun deleteAllFavorites()

}