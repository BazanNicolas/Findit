package com.products.app.data.repository

import com.products.app.data.mapper.toDomain
import com.products.app.data.remote.ProductsApi
import com.products.app.domain.model.ProductDetail
import com.products.app.domain.repository.ProductDetailRepository
import javax.inject.Inject

class ProductDetailRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi
) : ProductDetailRepository {
    
    override suspend fun getProductDetail(productId: String): Result<ProductDetail> {
        return try {
            val response = productsApi.getProductDetail(productId)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
