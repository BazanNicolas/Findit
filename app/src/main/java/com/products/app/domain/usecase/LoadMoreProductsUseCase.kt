package com.products.app.domain.usecase

import com.products.app.core.AppResult
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
import javax.inject.Inject

/**
 * Use case for loading additional products for pagination.
 * 
 * This use case handles the business logic for loading more products beyond
 * the initial search results. It calculates the next offset based on the
 * current offset and limit, then performs a new search with the updated
 * pagination parameters.
 * 
 * The use case is typically used when implementing infinite scrolling or
 * "Load More" functionality in product lists.
 * 
 * @param repository The products repository for data access
 */
class LoadMoreProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    /**
     * Executes the load more products operation.
     * 
     * This method calculates the next offset for pagination and performs
     * a new search to retrieve additional products beyond the current results.
     * 
     * @param query The original search term
     * @param currentOffset The current offset (number of items already loaded)
     * @param limit The number of items to load in this batch
     * @return AppResult containing either the additional search results or an error message
     */
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
