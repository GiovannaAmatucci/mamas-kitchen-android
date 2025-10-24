package com.giovanna.amatucci.foodbook.data.remote.api


import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.network.TokenHttpClient
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.util.ApiConstants
import com.giovanna.amatucci.foodbook.util.LogMessages
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parametersOf
import timber.log.Timber


interface AuthApi {
    suspend fun getAccessToken(): TokenResponse
}

class AuthApiImpl(private val client: TokenHttpClient) : AuthApi {
    override suspend fun getAccessToken(): TokenResponse {
        Timber.d(LogMessages.AUTH_API_GET_TOKEN_REQUEST.format(BuildConfig.TOKEN_URL))
        return try {
            val response = client().post(ApiConstants.Methods.TOKEN) {
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
            Timber.d(LogMessages.AUTH_API_GET_TOKEN_SUCCESS)
            tokenResponse
        } catch (e: Exception) {
            Timber.e(e, LogMessages.AUTH_API_GET_TOKEN_FAILURE.format(e.message))
            throw e
        }
    }
}