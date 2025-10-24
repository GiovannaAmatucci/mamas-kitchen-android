package com.giovanna.amatucci.foodbook.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity
import net.sqlcipher.database.SupportFactory

@Database(entities = [TokenEntity::class, SearchEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tokenDao(): TokenDao
    abstract fun searchDao(): SearchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                System.loadLibrary("sqlcipher")
                val passphrase = BuildConfig.DB_PASSPHRASE.toByteArray()
                val factory = SupportFactory(passphrase)
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "foodbook_database"
                ).openHelperFactory(factory).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}