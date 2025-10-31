package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.ds.TokenStorage
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository


class TokenRepositoryImpl(private val tokenStorage: TokenStorage) : TokenRepository {
    override suspend fun saveToken(token: String) = tokenStorage.saveToken(token)

    override suspend fun getToken(): String? = tokenStorage.getToken()

    override suspend fun clearToken() = tokenStorage.clearToken()
}