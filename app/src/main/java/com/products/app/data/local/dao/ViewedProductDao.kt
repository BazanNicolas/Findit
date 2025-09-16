package com.products.app.data.local.dao

import androidx.room.*
import com.products.app.data.local.entity.ViewedProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ViewedProductDao {
    
    @Query("SELECT * FROM viewed_products ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentViewedProducts(limit: Int = 10): Flow<List<ViewedProductEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertViewedProduct(viewedProduct: ViewedProductEntity)
    
    @Query("DELETE FROM viewed_products WHERE timestamp < :cutoffTime")
    suspend fun deleteOldViewedProducts(cutoffTime: Long)
    
    @Query("DELETE FROM viewed_products")
    suspend fun clearAllViewedProducts()
}
