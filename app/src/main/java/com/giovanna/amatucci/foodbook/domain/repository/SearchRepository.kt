package com.giovanna.amatucci.foodbook.domain.repository

interface SearchRepository {
    suspend fun saveSearchQuery(query: String)
    suspend fun getSearchQueries(limit: Int): List<String>
    suspend fun clearSearchHistory()
}