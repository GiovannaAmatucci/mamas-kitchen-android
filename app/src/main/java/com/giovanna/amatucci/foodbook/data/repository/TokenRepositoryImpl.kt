package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.TokenDao
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class TokenRepositoryImpl(private val tokenDao: TokenDao) : TokenRepository {
    override suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO) {
            tokenDao.saveToken(TokenEntity(accessToken = token))
        }
    }

    override suspend fun getToken(): String? {
        return withContext(Dispatchers.IO) {
            tokenDao.getToken()?.accessToken
        }
    }

    override suspend fun clearToken() {
        withContext(Dispatchers.IO) {
            tokenDao.clearToken()
        }
    }
}