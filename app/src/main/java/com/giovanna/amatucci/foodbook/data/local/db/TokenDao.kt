package com.giovanna.amatucci.foodbook.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity

@Dao
interface TokenDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun saveToken(token: TokenEntity)

    @Query("SELECT * FROM auth_token WHERE id = 1")
    suspend fun getToken(): TokenEntity?

    @Query("DELETE FROM auth_token")
    suspend fun clearToken()
}