package com.products.app.data.local.dao

import androidx.room.*
import com.products.app.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for search history operations.
 * 
 * This interface defines database operations for managing search history entries.
 * It provides methods for retrieving, inserting, and deleting search history data
 * with reactive Flow support for real-time updates.
 */
@Dao
interface SearchHistoryDao {
    
    /**
     * Retrieves the most recent search history entries.
     * 
     * @param limit Maximum number of entries to return (default: 10)
     * @return Flow emitting a list of recent search history entries ordered by timestamp
     */
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistoryEntity>>
    
    /**
     * Retrieves search history entries that match the given query.
     * 
     * This method performs a partial match search on the query field,
     * useful for providing autocomplete suggestions based on previous searches.
     * 
     * @param query The search term to match against
     * @param limit Maximum number of entries to return (default: 10)
     * @return Flow emitting a list of matching search history entries
     */
    @Query("SELECT * FROM search_history WHERE `query` LIKE '%' || :query || '%' ORDER BY timestamp DESC LIMIT :limit")
    fun getMatchingSearches(query: String, limit: Int = 10): Flow<List<SearchHistoryEntity>>
    
    /**
     * Inserts a new search history entry or replaces an existing one.
     * 
     * Uses REPLACE strategy to handle conflicts when the same query already exists.
     * 
     * @param search The search history entry to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)
    
    /**
     * Deletes a specific search history entry.
     * 
     * @param search The search history entry to delete
     */
    @Delete
    suspend fun deleteSearch(search: SearchHistoryEntity)
    
    /**
     * Clears all search history entries from the database.
     * 
     * This operation removes all stored search history data.
     */
    @Query("DELETE FROM search_history")
    suspend fun clearAllSearches()
}
