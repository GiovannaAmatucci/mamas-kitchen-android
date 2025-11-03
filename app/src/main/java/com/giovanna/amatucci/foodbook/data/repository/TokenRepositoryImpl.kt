package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.ds.TokenStorage
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository


class TokenRepositoryImpl(private val tokenStorage: TokenStorage) : TokenRepository {
    override suspend fun saveToken(token: String, expiresIn: Int) =
        tokenStorage.saveToken(token, expiresIn = expiresIn)

    override suspend fun getToken(): String? = tokenStorage.getToken()
    override suspend fun clearToken() = tokenStorage.clearToken()
    override suspend fun updateToken(token: String, expiresIn: Int) {
        clearToken()
        saveToken(token = token, expiresIn = expiresIn)
    }

    override suspend fun isTokenExpired(): Boolean = tokenStorage.isTokenExpired()

}