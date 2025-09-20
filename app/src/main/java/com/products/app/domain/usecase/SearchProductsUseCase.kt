package com.products.app.domain.usecase

import com.products.app.core.AppResult
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
import javax.inject.Inject

/**
 * Use case for searching products using the MercadoLibre API.
 * 
 * This use case handles the business logic for product searches, including
 * query validation and pagination support. It acts as an intermediary
 * between the presentation layer and the data layer, ensuring that business
 * rules are applied consistently.
 * 
 * The use case follows the Clean Architecture principle by encapsulating
 * business logic and providing a clean interface for the presentation layer.
 * 
 * @param repository The products repository for data access
 */
class SearchProductsUseCase @Inject constructor(
    private val repository: ProductsRepository
) {
    /**
     * Executes a product search with the given parameters.
     * 
     * This method performs a product search using the provided query string
     * and pagination parameters. The query is automatically trimmed to remove
     * leading and trailing whitespace.
     * 
     * @param query The search term entered by the user
     * @param offset The number of items to skip for pagination (default: 0)
     * @param limit The maximum number of items to return (default: 10)
     * @return AppResult containing either the search results or an error message
     */
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
