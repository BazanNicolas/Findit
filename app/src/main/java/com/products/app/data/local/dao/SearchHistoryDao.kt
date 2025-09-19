package com.products.app.data.local.dao

import androidx.room.*
import com.products.app.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentSearches(limit: Int = 10): Flow<List<SearchHistoryEntity>>
    
    @Query("SELECT * FROM search_history WHERE `query` LIKE '%' || :query || '%' ORDER BY timestamp DESC LIMIT :limit")
    fun getMatchingSearches(query: String, limit: Int = 10): Flow<List<SearchHistoryEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: SearchHistoryEntity)
    
    @Delete
    suspend fun deleteSearch(search: SearchHistoryEntity)
    
    @Query("DELETE FROM search_history")
    suspend fun clearAllSearches()
}
