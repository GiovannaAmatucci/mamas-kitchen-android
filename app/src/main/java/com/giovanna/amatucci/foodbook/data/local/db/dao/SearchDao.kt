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

    @Query("SELECT query_text FROM searchEntity ORDER BY timestamp_millis DESC LIMIT :limit")
    suspend fun getRecentQueries(limit: Int = 5): List<String>

    @Query("DELETE FROM searchEntity")
    suspend fun clearHistory()
}
