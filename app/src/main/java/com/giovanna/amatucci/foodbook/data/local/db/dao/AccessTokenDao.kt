package com.giovanna.amatucci.foodbook.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity

@Dao
interface AccessTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToken(token: TokenEntity)
    @Query("SELECT * FROM tokenEntity WHERE id = :id LIMIT 1")
    suspend fun getToken(id: String = "default_token"): TokenEntity?
    @Query("DELETE FROM tokenEntity WHERE id = :id")
    suspend fun deleteToken(id: String = "default_token")
}