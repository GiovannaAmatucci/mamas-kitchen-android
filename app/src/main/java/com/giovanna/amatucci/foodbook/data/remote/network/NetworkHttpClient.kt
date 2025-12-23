package com.giovanna.amatucci.foodbook.data.remote.network

import android.content.Context
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.NoConnectivityException
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.TAG
import com.giovanna.amatucci.foodbook.util.isInternetAvailable
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
    private val contextNet: Context,
    private val baseHostUrl: String,
    private val requestTimeout: Long,
    private val connectTimeout: Long,
    private val auth: AuthRepository,
    private val token: TokenRepository,
    private val isDebug: Boolean,
    private val logWriter: LogWriter
) : NetworkHttpClient {
    private val mutex = Mutex()
    private suspend fun fetchAndSaveNewToken(): BearerTokens? {
        val result = auth.fetchAndSaveToken()
        logWriter.d(TAG.NETWORK_HTTP_CLIENT, LogMessages.KTOR_REFRESH_MUTEX_EXECUTE)
        return if (result is ResultWrapper.Success) {
            logWriter.d(TAG.NETWORK_HTTP_CLIENT, LogMessages.KTOR_REFRESH_SUCCESS)
            result.data.accessToken?.let { newAccessToken ->
                BearerTokens(newAccessToken, "")
            }
        } else {
            logWriter.e(TAG.NETWORK_HTTP_CLIENT, LogMessages.KTOR_REFRESH_FAILURE)
            token.clearToken()
            null
        }
    }

    override fun invoke(): HttpClient {
        return if (!contextNet.isInternetAvailable()) {
            throw NoConnectivityException()
        } else {
            HttpClient(Android) {
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
                            mutex.withLock {
                                logWriter.d(TAG.NETWORK_HTTP_CLIENT, LogMessages.KTOR_LOAD_START)
                                val validToken = token.getValidAccessToken()

                                if (validToken != null) {
                                    logWriter.d(
                                        TAG.NETWORK_HTTP_CLIENT, LogMessages.KTOR_LOAD_SUCCESS
                                    )
                                    return@withLock BearerTokens(validToken, "")
                                }

                                logWriter.w(TAG.NETWORK_HTTP_CLIENT, LogMessages.KTOR_LOAD_FAILURE)
                                return@withLock fetchAndSaveNewToken()
                            }
                        }
                        refreshTokens {
                            mutex.withLock {
                                logWriter.w(TAG.NETWORK_HTTP_CLIENT, LogMessages.KTOR_REFRESH_START)
                                val currentTokenInDb = token.getValidAccessToken()
                                val oldTokenThatFailed = oldTokens?.accessToken

                                if (oldTokenThatFailed != currentTokenInDb && currentTokenInDb != null) {
                                    logWriter.d(
                                        TAG.NETWORK_HTTP_CLIENT, LogMessages.KTOR_REFRESH_MUTEX_WAIT
                                    )
                                    return@withLock BearerTokens(currentTokenInDb, "")
                                }
                                return@withLock fetchAndSaveNewToken()
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
                            if (isDebug) logWriter.d(TAG.NETWORK_HTTP_CLIENT, message)
                        }
                    }
                }
            }
        }
    }
}

