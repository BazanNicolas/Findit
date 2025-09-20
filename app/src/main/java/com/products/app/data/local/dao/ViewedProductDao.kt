package com.products.app.data.local.dao

import androidx.room.*
import com.products.app.data.local.entity.ViewedProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for viewed products operations.
 * 
 * This interface defines database operations for managing viewed products data.
 * It provides methods for retrieving, inserting, and deleting viewed product entries
 * with reactive Flow support for real-time updates.
 */
@Dao
interface ViewedProductDao {
    
    /**
     * Retrieves the most recently viewed products.
     * 
     * @param limit Maximum number of products to return (default: 10)
     * @return Flow emitting a list of recently viewed products ordered by timestamp
     */
    @Query("SELECT * FROM viewed_products ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentViewedProducts(limit: Int = 10): Flow<List<ViewedProductEntity>>
    
    /**
     * Inserts a new viewed product entry or replaces an existing one.
     * 
     * Uses REPLACE strategy to handle conflicts when the same product is viewed again,
     * updating the timestamp to reflect the most recent view.
     * 
     * @param viewedProduct The viewed product entry to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViewedProduct(viewedProduct: ViewedProductEntity)
    
    /**
     * Deletes a specific viewed product entry.
     * 
     * @param viewedProduct The viewed product entry to delete
     */
    @Delete
    suspend fun deleteViewedProduct(viewedProduct: ViewedProductEntity)
    
    /**
     * Deletes viewed products older than the specified cutoff time.
     * 
     * This method is useful for cleaning up old viewed products to prevent
     * the database from growing indefinitely.
     * 
     * @param cutoffTime Unix timestamp - products viewed before this time will be deleted
     */
    @Query("DELETE FROM viewed_products WHERE timestamp < :cutoffTime")
    suspend fun deleteOldViewedProducts(cutoffTime: Long)
    
    /**
     * Clears all viewed products from the database.
     * 
     * This operation removes all stored viewed products data.
     */
    @Query("DELETE FROM viewed_products")
    suspend fun clearAllViewedProducts()
}
