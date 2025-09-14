package com.products.app.domain.usecase

import com.products.app.core.AppResult
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    suspend operator fun invoke(
        query: String,
        offset: Int = 0,
        limit: Int = 10
    ): AppResult<ProductSearchResult> {
        return repository.search(
            query = query.trim(),
            offset = offset,
            limit = limit
        )
    }
}
