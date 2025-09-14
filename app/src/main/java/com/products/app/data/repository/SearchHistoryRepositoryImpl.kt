package com.products.app.data.repository

import com.products.app.data.local.dao.SearchHistoryDao
import com.products.app.data.mapper.toDomain
import com.products.app.data.mapper.toEntity
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {
    
    override fun getRecentSearches(limit: Int): Flow<List<SearchHistory>> {
        return searchHistoryDao.getRecentSearches(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override fun getMatchingSearches(query: String, limit: Int): Flow<List<SearchHistory>> {
        return searchHistoryDao.getMatchingSearches(query, limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    override suspend fun saveSearch(query: String) {
        val search = SearchHistory(query.trim(), System.currentTimeMillis())
        searchHistoryDao.insertSearch(search.toEntity())
    }
    
    override suspend fun deleteSearch(search: SearchHistory) {
        searchHistoryDao.deleteSearch(search.toEntity())
    }
    
    override suspend fun clearAllSearches() {
        searchHistoryDao.clearAllSearches()
    }
}
