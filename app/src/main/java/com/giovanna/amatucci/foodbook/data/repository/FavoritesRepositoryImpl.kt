package com.giovanna.amatucci.foodbook.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.giovanna.amatucci.foodbook.data.local.db.dao.FavoritesDao
import com.giovanna.amatucci.foodbook.data.remote.mapper.FavoritesMapper
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
    private val mapper: FavoritesMapper,
    private val dao: FavoritesDao,
    private val logWriter: LogWriter
) : FavoritesRepository {

    override suspend fun addFavorite(recipe: RecipeDetails) {
        logWriter.d(TAG.FAVORITES_REPOSITORY, LogMessages.REPO_FAVORITE_ADD_START.format(recipe))
        mapper.favoriteDomainToDto(recipe).let {
            dao.insertFavorite(it)
        }
    }

    override suspend fun getFavoriteDetails(recipeId: String): RecipeDetails? =
        dao.getFavoriteById(recipeId)?.let { favoriteEntity ->
            mapper.favoriteEntityToDetailsDomain(favoriteEntity)
        }

    override fun isFavorite(recipeId: String): Flow<Boolean> =
        dao.isFavorite(recipeId.toLong())

    override fun getFavorites(query: String): Flow<PagingData<RecipeItem>> {
        val preparedQuery = "%$query%"
        return Pager(
            config = PagingConfig(
                pageSize = RepositoryConstants.FAVORITE_REPOSITORY_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { dao.getAllFavoritesPaged(preparedQuery) }).flow.map { pagingData ->
            pagingData.map { favoriteEntity ->
                mapper.favoriteEntityToDomain(favoriteEntity)
            }
        }
    }

    override fun getLastFavorites(): Flow<List<RecipeItem>> {
        return dao.getLast3Favorites().map { list ->
            list.map { mapper.favoriteEntityToDomain(it) }
        }
    }

    override suspend fun removeFavorite(recipeId: String) {
        logWriter.d(
            TAG.FAVORITES_REPOSITORY, LogMessages.REPO_FAVORITE_REMOVE_START.format(recipeId)
        )
        dao.deleteFavorite(recipeId)
    }

    override suspend fun deleteAllFavorites() {
        dao.deleteAllFavorites()
    }
}
