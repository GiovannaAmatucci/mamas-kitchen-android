package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.giovanna.amatucci.foodbook.data.local.db.dao.FavoriteDao
import com.giovanna.amatucci.foodbook.data.remote.mapper.RecipeDataMapper
import com.giovanna.amatucci.foodbook.domain.model.RecipeDetails
import com.giovanna.amatucci.foodbook.domain.model.RecipeItem
import com.giovanna.amatucci.foodbook.domain.repository.FavoritesRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.RepositoryConstants
import com.giovanna.amatucci.foodbook.util.constants.TAG
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepositoryImpl(
    private val mapper: RecipeDataMapper,
    private val favoriteDao: FavoriteDao,
    private val logWriter: LogWriter
) : FavoritesRepository {

    override suspend fun addFavorite(
        recipe: RecipeDetails
    ) {
        logWriter.d(TAG.FAVORITES_REPOSITORY, LogMessages.REPO_FAVORITE_ADD_START.format(recipe))
        mapper.favoriteDomainToDto(recipe).let {
            favoriteDao.insertFavorite(it)
        }
    }

    override suspend fun getFavoriteDetails(recipeId: String): RecipeDetails? =
        favoriteDao.getFavoriteById(recipeId)?.let { favoriteEntity ->
            mapper.favoriteEntityToDetailsDomain(favoriteEntity)
        }

    override fun isFavorite(recipeId: String): Flow<Boolean> =
        favoriteDao.isFavorite(recipeId.toLong())

    override fun getFavorites(query: String): Flow<PagingData<RecipeItem>> {
        val preparedQuery = "%$query%"
        return Pager(
            config = PagingConfig(
                pageSize = RepositoryConstants.FAVORITE_REPOSITORY_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { favoriteDao.getAllFavoritesPaged(preparedQuery) }).flow.map { pagingData ->
            pagingData.map { favoriteEntity ->
                mapper.favoriteEntityToDomain(favoriteEntity)
            }
        }
    }

    override suspend fun removeFavorite(recipeId: String) {
        logWriter.d(
            TAG.FAVORITES_REPOSITORY, LogMessages.REPO_FAVORITE_REMOVE_START.format(recipeId)
        )
        favoriteDao.deleteFavorite(recipeId)
    }

    override suspend fun deleteAllFavorites() {
        favoriteDao.deleteAllFavorites()
    }
}
