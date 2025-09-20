package com.products.app.domain.repository

import com.products.app.domain.model.ViewedProduct
import kotlinx.coroutines.flow.Flow

interface ViewedProductRepository {
    fun getRecentViewedProducts(limit: Int = 10): Flow<List<ViewedProduct>>
    suspend fun saveViewedProduct(viewedProduct: ViewedProduct)
    suspend fun deleteViewedProduct(viewedProduct: ViewedProduct)
    suspend fun clearAllViewedProducts()
    suspend fun clearOldViewedProducts()
}
