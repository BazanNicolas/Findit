package com.products.app.domain.repository

import com.products.app.core.AppResult
import com.products.app.domain.model.ProductDetail

interface ProductDetailRepository {
    suspend fun getProductDetail(productId: String): AppResult<ProductDetail>
}
