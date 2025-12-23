package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.dao.SearchDao
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository
import com.giovanna.amatucci.foodbook.util.LogWriter
import com.giovanna.amatucci.foodbook.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.util.constants.RepositoryConstants
import com.giovanna.amatucci.foodbook.util.constants.TAG

class SearchRepositoryImpl(
    private val dao: SearchDao, private val logWriter: LogWriter
) : SearchRepository {
    override suspend fun saveSearchQuery(query: String) {
        try {
            if (query.isNotBlank()) {
                val currentHistory = dao.getSearchHistory()
                val oldQueries = currentHistory?.queries?.toMutableList() ?: mutableListOf()
                oldQueries.remove(query)
                oldQueries.add(RepositoryConstants.SEARCH_REPOSITORY_OLD_QUERY_ADD_INDEX, query)
                val newQueries =
                    oldQueries.take(RepositoryConstants.SEARCH_REPOSITORY_NEW_QUERY_TAKE)
                val newHistory = SearchEntity(id = currentHistory?.id ?: 0, queries = newQueries)
                dao.insertSearch(newHistory)
            }
        } catch (e: Exception) {
            logWriter.w(TAG.SEARCH_REPOSITORY, LogMessages.SAVE_QUERY_FAILURE.format(query), e)
        }
    }
    override suspend fun getSearchQueries(): List<String> {
        return try {
            val currentHistory = dao.getSearchHistory()

            if (currentHistory?.queries.isNullOrEmpty()) {
                emptyList()
            } else {
                currentHistory.queries
            }
        } catch (e: Exception) {
            logWriter.w(TAG.SEARCH_REPOSITORY, LogMessages.GET_QUERIES_FAILURE, e)
            emptyList()
        }
    }
    override suspend fun clearSearchHistory() {
        try {
            val currentHistory = dao.getSearchHistory()
            val emptyHistory = SearchEntity(
                id = currentHistory?.id ?: 0, queries = emptyList()
            )
            dao.insertSearch(emptyHistory)
        } catch (e: Exception) {
            logWriter.w(TAG.SEARCH_REPOSITORY, LogMessages.CLEAR_HISTORY_FAILURE, e)
        }
    }
}