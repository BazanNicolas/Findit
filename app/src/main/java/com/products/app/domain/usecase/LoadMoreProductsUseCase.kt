package com.products.app.domain.usecase

import com.products.app.core.AppResult
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
import javax.inject.Inject

class LoadMoreProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(
        query: String,
        currentOffset: Int,
        limit: Int
    ): AppResult<ProductSearchResult> {
        val nextOffset = currentOffset + limit
        return repository.search(
            query = query.trim(),
            offset = nextOffset,
            limit = limit
        )
    }
}
