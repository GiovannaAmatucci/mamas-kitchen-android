package com.giovanna.amatucci.foodbook.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchEntity)
    @Query("SELECT * FROM searchEntity")
    suspend fun getSearchHistory(): SearchEntity?
}