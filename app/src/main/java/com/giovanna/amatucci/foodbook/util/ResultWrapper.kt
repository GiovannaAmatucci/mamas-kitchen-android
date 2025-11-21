package com.giovanna.amatucci.foodbook.util

sealed class ResultWrapper<out T> {
    data class Success<T>(val data: T) : ResultWrapper<T>()
    data class Error(val message: String, val code: Int? = null) : ResultWrapper<Nothing>()
    data class Exception(val exception: Throwable) : ResultWrapper<Nothing>()
}