package com.giovanna.amatucci.foodbook.data.remote.api

import com.giovanna.amatucci.foodbook.BuildConfig
import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.data.remote.network.TokenHttpClient
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import com.giovanna.amatucci.foodbook.util.constants.ApiConstants
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.TAG.AUTH_API
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.parametersOf
import org.koin.core.annotation.Single

interface AuthApi {
    suspend fun getAccessToken(): ResultWrapper<TokenResponse>
}

@Single(binds = [AuthApi::class])
class AuthApiImpl(
    logWriter: LogWriter, private val client: TokenHttpClient
) : BaseApi(logWriter), AuthApi {
    override val tag: String = AUTH_API

    override suspend fun getAccessToken(): ResultWrapper<TokenResponse> {
        logWriter.d(tag, LogMessages.AUTH_TOKEN_REQUEST)
        return safeApiCall {
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
            logWriter.d(tag, LogMessages.AUTH_TOKEN_SUCCESS)
            response.body()
        }
    }
}