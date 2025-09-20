package com.products.app.data.repository

import com.products.app.data.local.dao.ViewedProductDao
import com.products.app.data.mapper.toDomain
import com.products.app.data.mapper.toEntity
import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Implementation of ViewedProductRepository for managing recently viewed products.
 * 
 * This repository handles all operations related to tracking and retrieving
 * recently viewed products using Room database. It provides persistent storage
 * for user viewing history, allowing quick access to previously viewed products
 * and automatic cleanup of old entries.
 * 
 * The repository uses Room DAOs for database operations and converts between
 * entity and domain models to maintain clean separation between data and domain layers.
 * It includes automatic cleanup functionality to manage storage space by removing
 * old viewed products after a configurable time period.
 * 
 * @param viewedProductDao The Room DAO for viewed products database operations
 */
class ViewedProductRepositoryImpl @Inject constructor(
    private val viewedProductDao: ViewedProductDao
) : ViewedProductRepository {
    
    /**
     * Retrieves the most recently viewed products.
     * 
     * Returns a Flow of viewed products ordered by view timestamp (most recent first).
     * The results are limited to the specified number of items and automatically
     * converted from database entities to domain models.
     * 
     * @param limit Maximum number of recent viewed products to return
     * @return Flow emitting a list of recent ViewedProduct items
     */
    override fun getRecentViewedProducts(limit: Int): Flow<List<ViewedProduct>> {
        return viewedProductDao.getRecentViewedProducts(limit)
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    /**
     * Saves a viewed product to the history.
     * 
     * Stores the product information with the current timestamp when a user
     * views a product detail page. This method is typically called when
     * navigating to a product detail screen to maintain the user's viewing history.
     * 
     * @param viewedProduct The ViewedProduct item to save
     */
    override suspend fun saveViewedProduct(viewedProduct: ViewedProduct) {
        viewedProductDao.insertViewedProduct(viewedProduct.toEntity())
    }
    
    /**
     * Removes a specific viewed product from the history.
     * 
     * Deletes the specified viewed product from the database. This is
     * typically used when a user manually removes a product from their
     * recently viewed list.
     * 
     * @param viewedProduct The ViewedProduct item to delete
     */
    override suspend fun deleteViewedProduct(viewedProduct: ViewedProduct) {
        viewedProductDao.deleteViewedProduct(viewedProduct.toEntity())
    }
    
    /**
     * Clears all viewed product history.
     * 
     * Removes all stored viewed products from the database. This operation
     * is typically used when a user wants to clear their entire viewing history
     * for privacy or storage management purposes.
     */
    override suspend fun clearAllViewedProducts() {
        viewedProductDao.clearAllViewedProducts()
    }
    
    /**
     * Removes old viewed products from the history.
     * 
     * Automatically cleans up viewed products older than 30 days to manage
     * storage space and maintain performance. This method can be called
     * periodically or as part of app maintenance routines.
     */
    override suspend fun clearOldViewedProducts() {
        val cutoffTime = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L) // 30 days
        viewedProductDao.deleteOldViewedProducts(cutoffTime)
    }
}
