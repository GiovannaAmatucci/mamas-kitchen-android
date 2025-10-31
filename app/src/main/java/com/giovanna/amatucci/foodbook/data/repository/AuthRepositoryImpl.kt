package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.di.util.LogMessages
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenRepository: TokenRepository,
    private val logWriter: LogWriter
) : AuthRepository {
    companion object {
        private const val TAG = "AuthRepository"
    }

    override suspend fun fetchAndSaveToken(): ResultWrapper<TokenResponse> =
        withContext(Dispatchers.IO) {
            val apiResult = authApi.getAccessToken()
            when (apiResult) {
                is ResultWrapper.Success -> {
                    try {
                        val tokenData = apiResult.data
                        logWriter.d(
                            TAG, LogMessages.TOKEN_REPO_SAVE.format(tokenData.accessToken)
                        )
                        tokenRepository.saveToken(tokenData.accessToken)
                        apiResult
                    } catch (dbException: Exception) {
                        logWriter.e(
                            TAG, LogMessages.TOKEN_REPO_GET_NOT_FOUND.format(dbException.message)
                        )
                        ResultWrapper.Exception(dbException)
                    }
                }

                is ResultWrapper.Error -> {
                    logWriter.e(TAG, message = LogMessages.TOKEN_REPO_GET_NOT_FOUND)
                    apiResult
                }

                is ResultWrapper.Exception -> {
                    logWriter.e(
                        TAG,
                        LogMessages.TOKEN_REPO_GET_NOT_FOUND.format(apiResult.exception.message)
                    )
                    apiResult
                }
            }
        }
}