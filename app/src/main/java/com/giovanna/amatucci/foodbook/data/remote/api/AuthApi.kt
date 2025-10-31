package com.giovanna.amatucci.foodbook.data.remote.api


import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.di.util.ApiConstants
import com.giovanna.amatucci.foodbook.di.util.LogMessages
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.parametersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber


interface AuthApi {
    suspend fun getAccessToken(
    ): ResultWrapper<TokenResponse>
}

class AuthApiImpl(private val logWriter: LogWriter) : AuthApi {
    val client: HttpClient = HttpClient(Android) {
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
                host = BuildConfig.TOKEN_URL
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

    companion object {
        private const val TAG = "AuthApi"
    }

    override suspend fun getAccessToken(): ResultWrapper<TokenResponse> {
        logWriter.d(TAG, LogMessages.AUTH_TOKEN_REQUEST)

        return try {
            val response = client.post(ApiConstants.Methods.TOKEN) {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    FormDataContent(
                        parametersOf(
                            ApiConstants.Params.GRANT_TYPE to listOf(ApiConstants.Values.CLIENT_CREDENTIALS),
                            ApiConstants.Params.SCOPE to listOf(ApiConstants.Values.BASIC),
                            ApiConstants.Params.CLIENT_ID to listOf(BuildConfig.FATSECRET_CLIENT_ID),
                            ApiConstants.Params.CLIENT_SECRET to listOf(BuildConfig.FATSECRET_CLIENT_SECRET)
                        )
                    )
                )
            }

            val tokenResponse: TokenResponse = response.body()
            logWriter.d(TAG, LogMessages.AUTH_TOKEN_SUCCESS)
            ResultWrapper.Success(tokenResponse)
        } catch (e: ResponseException) {
            val msg = LogMessages.AUTH_TOKEN_FAILURE.format(e.message)
            logWriter.e(TAG, msg)
            ResultWrapper.Error(
                msg, e.response.status.value
            )
        } catch (e: Exception) {
            val msg = LogMessages.AUTH_TOKEN_FAILURE.format(e.message)
            logWriter.e(TAG, msg)
            ResultWrapper.Exception(e)
        }
    }
}