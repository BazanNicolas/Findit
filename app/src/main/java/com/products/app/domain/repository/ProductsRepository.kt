package com.products.app.domain.repository

import com.products.app.core.AppResult
import com.products.app.domain.model.ProductSearchResult

interface ProductsRepository {
    suspend fun search(
        query: String,
        offset: Int,
        limit: Int
    ): AppResult<ProductSearchResult>
}