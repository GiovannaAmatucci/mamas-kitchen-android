package com.giovanna.amatucci.foodbook.data.remote.network

import com.giovanna.amatucci.foodbook.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
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

interface TokenHttpClient {
    operator fun invoke(): HttpClient
}

class TokenHttpClientImpl(private val baseUrl: String) : TokenHttpClient {
    override operator fun invoke() = HttpClient(Android) {
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
                host = baseUrl
            }
        }

        install(Logging) {
            level = if (BuildConfig.DEBUG_MODE) LogLevel.ALL else LogLevel.NONE
            logger = object : Logger {
                override fun log(message: String) {
                    if (BuildConfig.DEBUG_MODE) {
                        Timber.d(message)
                    }
                }
            }
        }
    }
}
