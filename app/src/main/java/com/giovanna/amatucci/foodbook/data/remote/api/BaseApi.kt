package com.giovanna.amatucci.foodbook.data.remote.api

import com.giovanna.amatucci.foodbook.di.util.LogMessages
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.serialization.ContentConvertException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

abstract class BaseApi(
    protected val logWriter: LogWriter
) {
    abstract val TAG: String

    protected suspend inline fun <reified T> safeApiCall(
        crossinline apiCall: suspend () -> T
    ): ResultWrapper<T> {
        return try {
            ResultWrapper.Success(apiCall())

        } catch (e: ClientRequestException) {
            val msg = LogMessages.API_ERROR_CLIENT.format(e.response.status)
            logWriter.e(TAG, msg, e)
            ResultWrapper.Error(msg, e.response.status.value)

        } catch (e: ServerResponseException) {
            val msg = LogMessages.API_ERROR_SERVER.format(e.response.status)
            logWriter.e(TAG, msg, e)
            ResultWrapper.Error(
                message = msg, code = e.response.status.value
            )

        } catch (e: IOException) {
            val msg = LogMessages.API_ERROR_NETWORK.format(e.message)
            logWriter.e(TAG, msg, e)
            ResultWrapper.Error(message = msg, code = -1)

        } catch (e: ContentConvertException) {
            val msg = LogMessages.API_ERROR_SERIALIZATION.format(e.message)
            logWriter.e(TAG, msg, e)
            ResultWrapper.Error(msg, -2)
        } catch (e: SerializationException) {
            val msg = LogMessages.API_ERROR_SERIALIZATION.format(e.message)
            logWriter.e(TAG, msg, e)
            ResultWrapper.Error(msg, -2)
        } catch (e: Exception) {
            val msg = LogMessages.API_ERROR_UNEXPECTED.format(e.message)
            logWriter.e(TAG, msg, e)
            ResultWrapper.Error(msg, -3)
        }
    }
}


