package com.giovanna.amatucci.foodbook.domain.usecase

import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository

interface CheckAuthenticationStatusUseCase {
    suspend operator fun invoke(): Boolean
}

class CheckAuthenticationStatusUseCaseImpl(private val tokenRepository: TokenRepository) :
    CheckAuthenticationStatusUseCase {
    override suspend operator fun invoke(): Boolean {
        return tokenRepository.getToken() != null
    }
}