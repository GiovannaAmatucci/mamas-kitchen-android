package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.dao.AccessTokenDao
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.util.CryptographyManager
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import java.util.Date
@Single(binds = [TokenRepository::class])
class TokenRepositoryImpl(
    private val dao: AccessTokenDao,
    private val cryptoManager: CryptographyManager,
    private val logWriter: LogWriter
) : TokenRepository {
    override suspend fun saveToken(response: TokenResponse) {
        withContext(Dispatchers.IO) {
            logWriter.d(TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_SAVE_START)
            try {
                val token = response.accessToken ?: return@withContext
                val (iv, encryptedToken) = cryptoManager.encrypt(token)
                val expiresInMillis = response.expiresIn * 1000L
                val expiresAt = System.currentTimeMillis() + expiresInMillis
                val entity = TokenEntity(
                    encryptedAccessToken = encryptedToken,
                    initializationVector = iv,
                    expiresAtMillis = expiresAt
                )
                dao.saveToken(entity)
                logWriter.d(
                    TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_SAVE_SUCCESS.format(
                        Date(expiresAt)
                    )
                )
            } catch (e: Exception) {
                logWriter.e(TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_SAVE_FAILURE, e)
            }
        }
    }

    override suspend fun getValidAccessToken(): String? {
        return withContext(Dispatchers.IO) {
            logWriter.d(TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_GET_START)
            dao.getToken().let { entity ->
                if (entity == null) {
                    logWriter.w(TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_GET_NOT_FOUND)
                    return@withContext null
                }
                if (System.currentTimeMillis() >= entity.expiresAtMillis) {
                    logWriter.w(TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_GET_EXPIRED)
                    return@withContext null
                }
                return@withContext try {
                    val decryptedToken = cryptoManager.decrypt(
                        iv = entity.initializationVector,
                        encryptedData = entity.encryptedAccessToken
                    )
                    logWriter.d(TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_GET_SUCCESS)
                    decryptedToken
                } catch (e: Exception) {
                    logWriter.e(
                        TAG.TOKEN_REPOSITORY,
                        LogMessages.TOKEN_REPO_DECRYPT_FAILURE.format(e.message)
                    )
                    null
                }
            }
        }
    }

    override suspend fun clearToken() {
        withContext(Dispatchers.IO) {
            try {
                dao.deleteToken()
                logWriter.w(TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_CLEAR)
            } catch (e: Exception) {
                logWriter.e(TAG.TOKEN_REPOSITORY, LogMessages.TOKEN_REPO_DECRYPT_FAILURE, e)
            }
        }
    }
}