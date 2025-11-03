package com.giovanna.amatucci.foodbook.data.local.ds

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class TokenDataStore(context: Context) : TokenStorage {
    private val dataStore = context.secureDataStore

    companion object {
        val TOKEN_EXPIRY_KEY = longPreferencesKey("auth_token_expiry")
        val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    override suspend fun saveToken(token: String, expiresIn: Int) {
        dataStore.edit { preferences ->
            val safetyMargin = 60 * 1000
            val expiryTime = System.currentTimeMillis() + (expiresIn * 1000) - safetyMargin

            preferences[TOKEN_KEY] = token
            preferences[TOKEN_EXPIRY_KEY] = expiryTime
        }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.firstOrNull()
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
            preferences.remove(TOKEN_EXPIRY_KEY)
        }
    }

    override suspend fun isTokenExpired(): Boolean {
        val expiryTime = dataStore.data.map { preferences ->
            preferences[TOKEN_EXPIRY_KEY]
        }.firstOrNull() ?: return true

        return System.currentTimeMillis() > expiryTime
    }
}
