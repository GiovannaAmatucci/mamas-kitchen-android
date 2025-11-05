package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.LogMessages
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

    override suspend fun fetchAndSaveToken(): ResultWrapper<TokenResponse> {
        return withContext(Dispatchers.IO) {
            logWriter.d(TAG, LogMessages.AUTH_TOKEN_REQUEST)
            authApi.getAccessToken().let { apiResult ->
                when (apiResult) {
                    is ResultWrapper.Success -> {
                        try {
                            tokenRepository.clearToken()
                            logWriter.d(
                                TAG, LogMessages.TOKEN_REPO_SAVE_SUCCESS
                            )
                            tokenRepository.saveToken(apiResult.data)
                            apiResult
                        } catch (dbException: Exception) {
                            logWriter.e(
                                TAG, LogMessages.AUTH_TOKEN_SAVE_FAILURE.format(dbException.message)
                            )
                            ResultWrapper.Exception(dbException)
                        }
                    }

                    is ResultWrapper.Error -> {
                        logWriter.e(TAG, message = LogMessages.AUTH_TOKEN_FAILURE)
                        apiResult
                    }

                    is ResultWrapper.Exception -> {
                        logWriter.e(
                            TAG, LogMessages.AUTH_TOKEN_FAILURE.format(apiResult.exception.message)
                        )
                        apiResult
                    }
                }
            }
        }
    }
}