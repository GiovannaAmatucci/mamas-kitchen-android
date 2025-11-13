package com.giovanna.amatucci.foodbook.data.repository

import com.giovanna.amatucci.foodbook.data.local.db.SearchDao
import com.giovanna.amatucci.foodbook.data.local.model.SearchEntity
import com.giovanna.amatucci.foodbook.di.util.LogWriter
import com.giovanna.amatucci.foodbook.di.util.constants.LogMessages
import com.giovanna.amatucci.foodbook.domain.repository.SearchRepository

class SearchRepositoryImpl(
    private val dao: SearchDao, private val logWriter: LogWriter
) : SearchRepository {

    private val TAG = "SearchRepository"

    override suspend fun saveSearchQuery(query: String) {
        try {
            if (query.isNotBlank()) {
                val currentHistory = dao.getSearchHistory()
                val oldQueries = currentHistory?.queries?.toMutableList() ?: mutableListOf()
                oldQueries.remove(query)
                oldQueries.add(0, query)
                val newQueries = oldQueries.take(10)
                val newHistory = SearchEntity(id = currentHistory?.id ?: 0, queries = newQueries)
                dao.insertSearch(newHistory)
            }
        } catch (e: Exception) {
            logWriter.w(TAG, LogMessages.SAVE_QUERY_FAILURE.format(query), e)
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
            logWriter.w(TAG, LogMessages.GET_QUERIES_FAILURE, e)
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
            logWriter.w(TAG, LogMessages.CLEAR_HISTORY_FAILURE, e)
        }
    }
}