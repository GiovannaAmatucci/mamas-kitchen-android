package com.giovanna.amatucci.foodbook.data.local.ds

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.secureDataStore: DataStore<Preferences> by preferencesDataStore(name = "secure_storage")

interface KeyStorage {
    suspend fun saveEncryptedKey(encryptedKey: String)
    suspend fun getEncryptedKey(): String?
}