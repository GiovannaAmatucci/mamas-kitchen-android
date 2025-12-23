package com.giovanna.amatucci.foodbook.domain.repository

import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse

interface TokenRepository {
    suspend fun saveToken(response: TokenResponse)
    suspend fun getValidAccessToken(): String?
    suspend fun clearToken()
}