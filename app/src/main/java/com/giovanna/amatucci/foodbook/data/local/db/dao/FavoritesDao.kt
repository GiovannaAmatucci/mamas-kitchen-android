package com.giovanna.amatucci.foodbook.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giovanna.amatucci.foodbook.data.local.model.FavoritesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favoritesEntity WHERE recipe_id = :recipeId LIMIT 1")
    suspend fun getFavoriteById(recipeId: String): FavoritesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(recipe: FavoritesEntity)

    @Query("DELETE FROM favoritesEntity WHERE recipe_id = :recipeId")
    suspend fun deleteFavorite(recipeId: String?)

    @Query("SELECT EXISTS(SELECT 1 FROM favoritesEntity WHERE recipe_id = :recipeId LIMIT 1)")
    fun isFavorite(recipeId: Long): Flow<Boolean>

    @Query("SELECT * FROM favoritesEntity WHERE name LIKE :query ORDER BY data_favorites DESC")
    fun getAllFavoritesPaged(query: String): PagingSource<Int, FavoritesEntity>

    @Query("SELECT * FROM favoritesEntity ORDER BY data_favorites DESC LIMIT 3")
    fun getLast3Favorites(): Flow<List<FavoritesEntity>>

    @Query("DELETE FROM favoritesEntity")
    suspend fun deleteAllFavorites()

}