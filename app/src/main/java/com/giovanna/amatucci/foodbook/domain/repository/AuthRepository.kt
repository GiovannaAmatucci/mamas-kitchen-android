package com.giovanna.amatucci.foodbook.domain.repository

import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.di.util.ResultWrapper

interface AuthRepository {
    suspend fun fetchAndSaveToken(): ResultWrapper<TokenResponse>
}