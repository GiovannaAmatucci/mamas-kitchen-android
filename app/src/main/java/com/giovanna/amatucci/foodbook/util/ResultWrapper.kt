package com.giovanna.amatucci.foodbook.util

/**
 * A sealed class wrapper for API responses, handling Success, Error, and Exception states.
 * @param T The type of data being returned in case of success.
 */
sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val message: String, val code: Int? = null) : ResultWrapper<Nothing>()
    data class Exception(val exception: Throwable) : ResultWrapper<Nothing>()
}