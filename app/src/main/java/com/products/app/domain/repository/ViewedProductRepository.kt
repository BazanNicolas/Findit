package com.products.app.domain.repository

import com.products.app.domain.model.ViewedProduct
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing viewed product operations.
 * 
 * This interface defines the contract for viewed product data access,
 * providing methods for tracking and retrieving recently viewed products.
 * It supports automatic cleanup of old entries to manage storage space.
 * 
 * The repository pattern abstracts data access details and provides
 * a clean interface for the domain layer to interact with viewed product data.
 */
interface ViewedProductRepository {
    /**
     * Retrieves recently viewed products.
     * 
     * @param limit Maximum number of viewed products to return (default: 10)
     * @return Flow containing the list of recently viewed products
     */
    fun getRecentViewedProducts(limit: Int = 10): Flow<List<ViewedProduct>>
    
    /**
     * Saves a viewed product to the viewing history.
     * 
     * @param viewedProduct The viewed product to save
     */
    suspend fun saveViewedProduct(viewedProduct: ViewedProduct)
    
    /**
     * Deletes a specific viewed product from the viewing history.
     * 
     * @param viewedProduct The viewed product to delete
     */
    suspend fun deleteViewedProduct(viewedProduct: ViewedProduct)
    
    /**
     * Clears all viewed product entries.
     */
    suspend fun clearAllViewedProducts()
    
    /**
     * Clears old viewed product entries based on age criteria.
     */
    suspend fun clearOldViewedProducts()
}
