package com.giovanna.amatucci.foodbook.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchEntity")
data class SearchEntity(
    @PrimaryKey
    @ColumnInfo(name = "query_text")
    val query: String,
    @ColumnInfo(name = "timestamp_millis")
    val timestamp: Long = System.currentTimeMillis()
)