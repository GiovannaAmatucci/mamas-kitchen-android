package com.giovanna.amatucci.foodbook.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giovanna.amatucci.foodbook.data.local.ds.CryptoManager
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import kotlinx.coroutines.runBlocking
import net.sqlcipher.database.SupportFactory

@Database(entities = [SearchEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, cryptoManager: CryptoManager): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val passphrase = runBlocking {
                    cryptoManager.getDecryptedDbKey()
                }

                val factory = SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "foodbook.db"
                ).openHelperFactory(factory).build()

                INSTANCE = instance
                instance
            }
        }
    }
}