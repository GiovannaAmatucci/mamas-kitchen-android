package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.util.LogMessages
import io.ktor.client.plugins.ClientRequestException
import timber.log.Timber

class AuthRepositoryImpl(
    private val authApi: AuthApi, private val tokenRepository: TokenRepository
) : AuthRepository {

    override suspend fun fetchAndSaveToken(): ResultWrapper<TokenResponse> {
        return try {
            val tokenResponse = authApi.getAccessToken()
            Timber.d(LogMessages.AUTH_API_GET_TOKEN_REQUEST, tokenResponse)
            tokenRepository.saveToken(tokenResponse.accessToken)
            ResultWrapper.Success(tokenResponse)
        } catch (e: Exception) {
            if (e is ClientRequestException) {
                Timber.e(e, LogMessages.AUTH_API_GET_TOKEN_FAILURE, e.response.status.value)
            }
            ResultWrapper.Exception(e)
        }
    }
}