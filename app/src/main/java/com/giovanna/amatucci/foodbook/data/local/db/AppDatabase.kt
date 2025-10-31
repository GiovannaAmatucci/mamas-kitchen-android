package com.giovanna.amatucci.foodbook.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.giovanna.amatucci.foodbook.data.local.ds.CryptoManager
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.sqlcipher.database.SupportFactory

@Database(entities = [SearchEntity::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        private val mutex = Mutex()

        suspend fun getDatabase(context: Context, cryptoManager: CryptoManager): AppDatabase {
            return INSTANCE ?: mutex.withLock {
                INSTANCE ?: buildDatabase(context, cryptoManager).also {
                    INSTANCE = it
                }
            }
        }

        private suspend fun buildDatabase(
            context: Context,
            cryptoManager: CryptoManager
        ): AppDatabase {
            val passphrase = cryptoManager.getDecryptedDbKey()
            val factory = SupportFactory(passphrase)

            return Room.databaseBuilder(
                context.applicationContext, AppDatabase::class.java, "foodbook.db"
            ).openHelperFactory(factory).build()
        }
    }
}