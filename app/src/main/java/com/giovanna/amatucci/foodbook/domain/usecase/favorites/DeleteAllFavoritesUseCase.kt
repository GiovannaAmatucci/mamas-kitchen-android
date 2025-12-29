package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import org.koin.core.annotation.Factory

interface DeleteAllFavoritesUseCase {
    suspend operator fun invoke()
}
@Factory(binds = [DeleteAllFavoritesUseCase::class])
class DeleteAllFavoritesUseCaseImpl(private val repository: FavoritesRepository) : DeleteAllFavoritesUseCase {
    override suspend operator fun invoke() = repository.deleteAllFavorites()
}