package com.giovanna.amatucci.foodbook.domain.repository

interface TokenRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()

}