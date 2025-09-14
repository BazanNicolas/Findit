package com.products.app.domain.repository

import com.products.app.domain.model.ProductDetail

interface ProductDetailRepository {
    suspend fun getProductDetail(productId: String): Result<ProductDetail>
}
