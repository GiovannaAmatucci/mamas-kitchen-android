package com.giovanna.amatucci.foodbook.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

interface KtorClient {
    operator fun invoke(): HttpClient
}

class KtorClientImpl(
    private val baseHostUrl: String,
    private val requestTimeout: Long,
    private val connectTimeout: Long,
    private val isDebug: Boolean
) : KtorClient {
    override fun invoke(): HttpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = baseHostUrl
            }
            header("Content-Type", "application/json")
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