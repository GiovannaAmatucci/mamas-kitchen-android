package com.giovanna.amatucci.foodbook.data.remote.network

import com.giovanna.amatucci.foodbook.di.util.LogMessages
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
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
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json


interface NetworkHttpClient {
    operator fun invoke(): HttpClient
}

class NetworkHttpClientImpl(
    private val baseHostUrl: String,
    private val requestTimeout: Long,
    private val connectTimeout: Long,
    private val auth: AuthRepository,
    private val token: TokenRepository,
    private val isDebug: Boolean,
    private val logWriter: LogWriter
) : NetworkHttpClient {
    companion object {
        private const val TAG = "NetworkHttpClient"
    }

    private val mutex = Mutex()

    override fun invoke(): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
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
                    val accessToken = token.getToken()
                    if (accessToken != null) {
                        BearerTokens(accessToken, "")
                    } else {
                        logWriter.w(TAG, LogMessages.KTOR_LOAD_TOKEN_FAILURE)
                        null

                    }
                }
                refreshTokens {
                    mutex.withLock {
                        logWriter.w(TAG, LogMessages.KTOR_REFRESH_TOKEN_START)
                        val oldTokenThatFailed = oldTokens?.accessToken
                        val currentTokenInDb = token.getToken()
                        if (oldTokenThatFailed != currentTokenInDb) {
                            currentTokenInDb?.let { token ->
                                logWriter.d(
                                    TAG, LogMessages.KTOR_REFRESH_TOKEN_SUCCESS.format(
                                        currentTokenInDb
                                    )
                                )
                                return@withLock BearerTokens(token, "")
                            }
                        }
                        val result = auth.fetchAndSaveToken()
                        if (result is ResultWrapper.Success) {
                            logWriter.d(TAG, LogMessages.KTOR_REFRESH_TOKEN_SUCCESS)
                            result.data.accessToken.let { BearerTokens(it, "") }
                        } else {
                            logWriter.e(TAG, LogMessages.KTOR_REFRESH_TOKEN_FAILURE)
                            token.clearToken()
                            null
                        }
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
                    if (isDebug) logWriter.d(TAG, message)
                }
            }
        }
    }
}