package com.giovanna.amatucci.foodbook.domain.usecase.auth

import com.giovanna.amatucci.foodbook.domain.repository.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

interface CheckAuthenticationStatusUseCase {
    suspend operator fun invoke(): Boolean
}
@Factory(binds = [CheckAuthenticationStatusUseCase::class])
class CheckAuthenticationStatusUseCaseImpl(
    private val tokenRepository: TokenRepository
) : CheckAuthenticationStatusUseCase {
    override suspend operator fun invoke(): Boolean = withContext(Dispatchers.IO) {
        tokenRepository.getValidAccessToken() != null
    }
}