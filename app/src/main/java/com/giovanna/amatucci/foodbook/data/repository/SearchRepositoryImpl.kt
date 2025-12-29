package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.dao.SearchDao
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.TAG
import org.koin.core.annotation.Single

@Single(binds = [SearchRepository::class])
class SearchRepositoryImpl(
    private val dao: SearchDao, private val logWriter: LogWriter
) : SearchRepository {
    override suspend fun saveSearchQuery(query: String) {
        try {
            if (query.isNotBlank()) {
                val entity = SearchEntity(
                    query = query, timestamp = System.currentTimeMillis()
                )
                dao.insertSearch(entity)
            }
        } catch (e: Exception) {
            logWriter.w(
                TAG.SEARCH_REPOSITORY,
                LogMessages.SAVE_QUERY_FAILURE.format(query),
                e
            )
        }
    }

    override suspend fun getSearchQueries(limit: Int): List<String> {
        return try {
            dao.getRecentQueries(limit)
        } catch (e: Exception) {
            logWriter.w(TAG.SEARCH_REPOSITORY, LogMessages.GET_QUERIES_FAILURE, e)
            emptyList()
        }
    }

    override suspend fun clearSearchHistory() {
        try {
            dao.clearHistory()
        } catch (e: Exception) {
            logWriter.w(TAG.SEARCH_REPOSITORY, LogMessages.CLEAR_HISTORY_FAILURE, e)
        }
    }
}