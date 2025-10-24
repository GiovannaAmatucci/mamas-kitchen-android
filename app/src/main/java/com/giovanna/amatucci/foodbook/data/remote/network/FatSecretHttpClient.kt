package com.giovanna.amatucci.foodbook.data.remote.network

import com.giovanna.amatucci.foodbook.data.remote.api.AuthApi
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.util.LogMessages
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

interface FatSecretApiHttpClient {
    operator fun invoke(): HttpClient
}

class FatSecretApiHttpClientImpl(
    private val baseHostUrl: String,
    private val requestTimeout: Long,
    private val connectTimeout: Long,
    private val isDebug: Boolean,
    private val authApi: AuthApi,
    private val tokenRepository: TokenRepository
) : FatSecretApiHttpClient {
    override fun invoke(): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            url {
                protocol = URLProtocol.HTTPS
                host = baseHostUrl
            }
        }

        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = tokenRepository.getToken()
                    if (accessToken != null) {
                        Timber.d(LogMessages.FATSECRET_AUTH_TOKEN_FROM_REPO)
                        BearerTokens(accessToken, "")
                    } else {
                        Timber.w(LogMessages.FATSECRET_AUTH_TOKEN_NOT_FOUND)
                        null
                    }
                }
                refreshTokens {
                    Timber.d(LogMessages.FATSECRET_AUTH_TOKEN_REFRESHING)
                    try {
                        val tokenResponse: TokenResponse = authApi.getAccessToken()
                        tokenRepository.saveToken(tokenResponse.accessToken)
                        Timber.d(LogMessages.FATSECRET_AUTH_TOKEN_REFRESHED.format(tokenResponse.tokenType))
                        BearerTokens(tokenResponse.accessToken, "")
                    } catch (e: Exception) {
                        Timber.e(
                            e, LogMessages.FATSECRET_AUTH_TOKEN_REFRESH_FAILED.format(
                                e.message ?: "Unknown error"
                            )
                        )
                        null
                    }
                }
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = requestTimeout
            connectTimeoutMillis = connectTimeout
            socketTimeoutMillis = connectTimeout
        }

        install(Logging) {
            level = if (isDebug) LogLevel.ALL else LogLevel.NONE
            logger = object : Logger {
                override fun log(message: String) {
                    if (isDebug) {
                        Timber.d(message)
                    }
                }
            }
        }
    }
}