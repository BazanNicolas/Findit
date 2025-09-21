package com.products.app.domain.repository

import com.products.app.domain.model.SearchHistory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing search history operations.
 * 
 * This interface defines the contract for search history data access,
 * providing methods for retrieving, saving, and managing search queries.
 * It supports both recent searches retrieval and matching searches for
 * autocomplete functionality.
 * 
 * The repository pattern abstracts data access details and provides
 * a clean interface for the domain layer to interact with search history data.
 */
interface SearchHistoryRepository {
    /**
     * Retrieves recent search history entries.
     * 
     * @param limit Maximum number of recent searches to return (default: 10)
     * @return Flow containing the list of recent search history entries
     */
    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistory>>
    
    /**
     * Retrieves search history entries that match the given query.
     * 
     * @param query The search term to match against existing searches
     * @param limit Maximum number of matching searches to return (default: 10)
     * @return Flow containing the list of matching search history entries
     */
    fun getMatchingSearches(query: String, limit: Int = 10): Flow<List<SearchHistory>>
    
    /**
     * Saves a new search query to the search history.
     * 
     * @param query The search query to save
     */
    suspend fun saveSearch(query: String)
    
    /**
     * Deletes a specific search entry from the search history.
     * 
     * @param search The search history entry to delete
     */
    suspend fun deleteSearch(search: SearchHistory)
    
    /**
     * Clears all search history entries.
     */
    suspend fun clearAllSearches()
}
