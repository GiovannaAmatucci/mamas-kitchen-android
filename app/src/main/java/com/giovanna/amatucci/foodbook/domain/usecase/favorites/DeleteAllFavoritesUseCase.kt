package com.giovanna.amatucci.foodbook.domain.usecase.favorites

import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository

interface DeleteAllFavoritesUseCase {
    suspend operator fun invoke()
}

class DeleteAllFavoritesUseCaseImpl(
    private val repository: FavoritesRepository
) : DeleteAllFavoritesUseCase {
    override suspend operator fun invoke() = repository.deleteAllFavorites()
}