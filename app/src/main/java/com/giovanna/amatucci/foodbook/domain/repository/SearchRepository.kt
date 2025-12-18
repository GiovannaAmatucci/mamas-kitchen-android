package com.giovanna.amatucci.foodbook.domain.repository

interface SearchRepository {
    suspend fun saveSearchQuery(query: String)
    suspend fun getSearchQueries(): List<String>
    suspend fun clearSearchHistory()
}