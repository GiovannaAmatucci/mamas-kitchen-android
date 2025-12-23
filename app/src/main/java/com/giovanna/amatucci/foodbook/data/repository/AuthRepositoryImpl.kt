package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.dao.AccessTokenDao
import com.giovanna.amatucci.foodbook.data.local.model.TokenEntity
import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.util.CryptographyManager
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.RepositoryConstants
import com.giovanna.amatucci.foodbook.util.constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val dao: AccessTokenDao,
    private val cryptoManager: CryptographyManager,
    private val logWriter: LogWriter
) : AuthRepository {
    override suspend fun fetchAndSaveToken(): ResultWrapper<TokenResponse> {
        return withContext(Dispatchers.IO) {
            logWriter.d(TAG.AUTH_REPOSITORY, LogMessages.AUTH_TOKEN_REQUEST)

            authApi.getAccessToken().let { apiResult ->
                when (apiResult) {
                    is ResultWrapper.Success -> {
                        try {
                            saveTokenToDatabase(apiResult.data)

                            logWriter.d(TAG.AUTH_REPOSITORY, LogMessages.TOKEN_REPO_SAVE_SUCCESS)
                            apiResult
                        } catch (e: Exception) {
                            logWriter.e(
                                TAG.AUTH_REPOSITORY,
                                LogMessages.AUTH_TOKEN_SAVE_FAILURE.format(e.message)
                            )
                            ResultWrapper.Exception(e)
                        }
                    }

                    is ResultWrapper.Error, is ResultWrapper.Exception -> {
                        logWriter.e(
                            TAG.AUTH_REPOSITORY,
                            message = LogMessages.AUTH_TOKEN_FAILURE.format(apiResult.toString())
                        )
                        apiResult
                    }
                }
            }
        }
    }

    private suspend fun saveTokenToDatabase(response: TokenResponse) {
        val token = response.accessToken ?: return
        val (iv, encryptedToken) = cryptoManager.encrypt(token)
        val expiresInMillis = response.expiresIn * RepositoryConstants.AUTH_REPOSITORY_EXPIRES_IN
        val expiresAt = System.currentTimeMillis() + expiresInMillis
        val entity = TokenEntity(
            encryptedAccessToken = encryptedToken,
            initializationVector = iv,
            expiresAtMillis = expiresAt
        )
        dao.saveToken(entity)

        logWriter.d(
            TAG.AUTH_REPOSITORY, LogMessages.TOKEN_REPO_SAVE_SUCCESS.format(
                Date(expiresAt)
            )
        )
    }
}