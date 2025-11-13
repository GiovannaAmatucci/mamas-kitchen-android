package com.giovanna.amatucci.foodbook.domain.usecase.favorite

import com.giovanna.amatucci.foodbook.domain.repository.FavoriteRepository

interface DeleteAllFavoritesUseCase {
    suspend operator fun invoke()
}


class DeleteAllFavoritesUseCaseImpl(
    private val repository: FavoriteRepository
) : DeleteAllFavoritesUseCase {
    override suspend operator fun invoke() = repository.deleteAllFavorites()
}