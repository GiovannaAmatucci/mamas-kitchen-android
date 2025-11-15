package com.giovanna.amatucci.foodbook.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giovanna.amatucci.foodbook.data.local.model.FavoriteEntity
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity
import com.giovanna.amatucci.foodbook.di.util.Converters

@Database(
    entities = [SearchEntity::class, TokenEntity::class, FavoriteEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao
    abstract fun accessTokenDao(): AccessTokenDao

    abstract fun favoriteDao(): FavoriteDao
}


