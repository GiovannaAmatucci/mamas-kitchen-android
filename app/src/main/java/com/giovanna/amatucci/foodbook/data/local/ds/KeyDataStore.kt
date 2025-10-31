package com.giovanna.amatucci.foodbook.data.local.ds

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class KeyDataStore(context: Context) : KeyStorage {

    private val dataStore = context.secureDataStore

    companion object {
        private val ENCRYPTED_DB_KEY = stringPreferencesKey("encrypted_db_key")
    }

    override suspend fun saveEncryptedKey(encryptedKey: String) {
        dataStore.edit { preferences ->
            preferences[ENCRYPTED_DB_KEY] = encryptedKey
        }
    }

    override suspend fun getEncryptedKey(): String? {
        return dataStore.data.map { preferences ->
            preferences[ENCRYPTED_DB_KEY]
        }.first()
    }
}