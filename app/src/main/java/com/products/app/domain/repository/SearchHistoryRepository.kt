package com.products.app.domain.repository

import com.products.app.domain.model.SearchHistory
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistory>>
    fun getMatchingSearches(query: String, limit: Int = 10): Flow<List<SearchHistory>>
    suspend fun saveSearch(query: String)
    suspend fun deleteSearch(search: SearchHistory)
    suspend fun clearAllSearches()
}
