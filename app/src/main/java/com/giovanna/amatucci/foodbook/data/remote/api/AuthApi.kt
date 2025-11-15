package com.giovanna.amatucci.foodbook.data.remote.api


import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.data.remote.network.TokenHttpClient
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import com.giovanna.amatucci.foodbook.di.util.constants.ApiConstants
import com.giovanna.amatucci.foodbook.di.util.constants.LogMessages
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.basicAuth
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parametersOf


interface AuthApi {
    suspend fun getAccessToken(
    ): ResultWrapper<TokenResponse>
}


class AuthApiImpl(private val logWriter: LogWriter, private val client: TokenHttpClient) : AuthApi {
    companion object {
        private const val TAG = "AuthApi"
    }
    override suspend fun getAccessToken(
    ): ResultWrapper<TokenResponse> {
        logWriter.d(TAG, LogMessages.AUTH_TOKEN_REQUEST)
        return try {
            val response = client().post(ApiConstants.Methods.TOKEN) {
                basicAuth(BuildConfig.FATSECRET_CLIENT_ID, BuildConfig.FATSECRET_CLIENT_SECRET)
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    FormDataContent(
                        parametersOf(
                            ApiConstants.Params.GRANT_TYPE to listOf(ApiConstants.Values.CLIENT_CREDENTIALS),
                            ApiConstants.Params.SCOPE to listOf(ApiConstants.Values.BASIC)
                        )
                    )
                )
            }
            logWriter.d(TAG, LogMessages.AUTH_TOKEN_SUCCESS)
            ResultWrapper.Success(response.body())
        } catch (e: ResponseException) {
            val msg = LogMessages.AUTH_TOKEN_FAILURE.format(e.message)
            logWriter.e(TAG, msg)
            ResultWrapper.Error(
                msg, e.response.status.value
            )
        } catch (e: Exception) {
            val msg = LogMessages.AUTH_TOKEN_FAILURE.format(e.message)
            logWriter.e(TAG, msg, e)
            ResultWrapper.Exception(e)
        }
    }
}

