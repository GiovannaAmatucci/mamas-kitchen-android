package com.giovanna.amatucci.foodbook.data.local.ds

interface TokenStorage {
    suspend fun saveToken(token: String, expiresIn: Int)
    suspend fun getToken(): String?
    suspend fun clearToken()

    suspend fun isTokenExpired(): Boolean
}

