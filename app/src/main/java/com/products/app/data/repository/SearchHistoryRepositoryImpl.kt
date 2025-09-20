package com.products.app.data.repository

import com.products.app.data.local.dao.SearchHistoryDao
import com.products.app.data.mapper.toDomain
import com.products.app.data.mapper.toEntity
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of SearchHistoryRepository for managing local search history.
 * 
 * This repository handles all search history operations using Room database,
 * providing persistent storage for user search queries. It manages the lifecycle
 * of search history data, including saving new searches, retrieving recent searches,
 * and providing search suggestions based on partial matches.
 * 
 * The repository uses Room DAOs for database operations and converts between
 * entity and domain models to maintain clean separation between data and domain layers.
 * All operations are performed on background threads to avoid blocking the UI.
 * 
 * @param searchHistoryDao The Room DAO for search history database operations
 */
class SearchHistoryRepositoryImpl @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : SearchHistoryRepository {
    
    /**
     * Retrieves the most recent search queries.
     * 
     * Returns a Flow of search history items ordered by timestamp (most recent first).
     * The results are limited to the specified number of items and automatically
     * converted from database entities to domain models.
     * 
     * @param limit Maximum number of recent searches to return
     * @return Flow emitting a list of recent SearchHistory items
     */
    override fun getRecentSearches(limit: Int): Flow<List<SearchHistory>> {
        return searchHistoryDao.getRecentSearches(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    /**
     * Retrieves search queries that match the given query string.
     * 
     * Performs a partial match search on stored search queries, useful for
     * providing autocomplete suggestions based on previous searches. The search
     * is case-insensitive and returns results ordered by relevance and recency.
     * 
     * @param query The search string to match against stored queries
     * @param limit Maximum number of matching searches to return
     * @return Flow emitting a list of matching SearchHistory items
     */
    override fun getMatchingSearches(query: String, limit: Int): Flow<List<SearchHistory>> {
        return searchHistoryDao.getMatchingSearches(query, limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }
    
    /**
     * Saves a new search query to the history.
     * 
     * Stores the search query with the current timestamp. The query is trimmed
     * to remove leading/trailing whitespace before storage. This method is
     * typically called when a user performs a search to maintain their search history.
     * 
     * @param query The search query to save
     */
    override suspend fun saveSearch(query: String) {
        val search = SearchHistory(query.trim(), System.currentTimeMillis())
        searchHistoryDao.insertSearch(search.toEntity())
    }
    
    /**
     * Removes a specific search from the history.
     * 
     * Deletes the specified search history item from the database. This is
     * typically used when a user manually removes a search from their history.
     * 
     * @param search The SearchHistory item to delete
     */
    override suspend fun deleteSearch(search: SearchHistory) {
        searchHistoryDao.deleteSearch(search.toEntity())
    }
    
    /**
     * Clears all search history.
     * 
     * Removes all stored search queries from the database. This operation
     * is typically used when a user wants to clear their entire search history
     * for privacy or storage management purposes.
     */
    override suspend fun clearAllSearches() {
        searchHistoryDao.clearAllSearches()
    }
}
