package com.products.app.data.repository

import com.products.app.data.local.dao.ViewedProductDao
import com.products.app.data.mapper.toDomain
import com.products.app.data.mapper.toEntity
import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ViewedProductRepositoryImpl @Inject constructor(
    private val viewedProductDao: ViewedProductDao
) : ViewedProductRepository {
    
    override fun getRecentViewedProducts(limit: Int): Flow<List<ViewedProduct>> {
        return viewedProductDao.getRecentViewedProducts(limit)
            .map { entities -> entities.map { it.toDomain() } }
    }
    
    override suspend fun saveViewedProduct(viewedProduct: ViewedProduct) {
        viewedProductDao.insertViewedProduct(viewedProduct.toEntity())
    }
    
    override suspend fun deleteViewedProduct(viewedProduct: ViewedProduct) {
        viewedProductDao.deleteViewedProduct(viewedProduct.toEntity())
    }
    
    override suspend fun clearAllViewedProducts() {
        viewedProductDao.clearAllViewedProducts()
    }
    
    override suspend fun clearOldViewedProducts() {
        val cutoffTime = System.currentTimeMillis() - (30 * 24 * 60 * 60 * 1000L) // 30 days
        viewedProductDao.deleteOldViewedProducts(cutoffTime)
    }
}
