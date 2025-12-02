package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenRepository: TokenRepository,
    private val logWriter: LogWriter
) : AuthRepository {

    override suspend fun fetchAndSaveToken(): ResultWrapper<TokenResponse> {
        return withContext(Dispatchers.IO) {
            logWriter.d(TAG.AUTH_REPOSITORY, LogMessages.AUTH_TOKEN_REQUEST)
            authApi.getAccessToken().let { apiResult ->
                when (apiResult) {
                    is ResultWrapper.Success -> {
                        try {
                            tokenRepository.clearToken()
                            logWriter.d(
                                TAG.AUTH_REPOSITORY, LogMessages.TOKEN_REPO_SAVE_SUCCESS
                            )
                            tokenRepository.saveToken(apiResult.data)
                            apiResult
                        } catch (dbException: Exception) {
                            logWriter.e(
                                TAG.AUTH_REPOSITORY,
                                LogMessages.AUTH_TOKEN_SAVE_FAILURE.format(dbException.message)
                            )
                            ResultWrapper.Exception(dbException)
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
}