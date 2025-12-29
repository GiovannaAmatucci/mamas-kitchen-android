package com.giovanna.amatucci.foodbook.domain.usecase.auth

import com.giovanna.amatucci.foodbook.data.remote.model.TokenResponse
import com.giovanna.amatucci.foodbook.domain.repository.AuthRepository
import com.giovanna.amatucci.foodbook.util.ResultWrapper
import org.koin.core.annotation.Factory

interface FetchAndSaveTokenUseCase {
    suspend operator fun invoke(): ResultWrapper<TokenResponse>
}

@Factory(binds = [FetchAndSaveTokenUseCase::class])
class FetchAndSaveTokenUseCaseImpl(private val authRepository: AuthRepository) : FetchAndSaveTokenUseCase {
    override suspend operator fun invoke(): ResultWrapper<TokenResponse> = authRepository.fetchAndSaveToken()
}