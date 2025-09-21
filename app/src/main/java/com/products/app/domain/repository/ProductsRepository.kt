package com.products.app.domain.repository

import com.products.app.core.AppResult
import com.products.app.domain.model.ProductSearchResult

/**
 * Repository interface for managing product search operations.
 * 
 * This interface defines the contract for product search data access,
 * providing methods for searching products with pagination support.
 * It abstracts the data source details and provides a clean interface
 * for the domain layer to interact with product search functionality.
 * 
 * The repository pattern ensures separation of concerns and makes the
 * codebase more testable and maintainable.
 */
interface ProductsRepository {
    /**
     * Searches for products using the provided parameters.
     * 
     * @param query The search term entered by the user
     * @param offset The number of items to skip for pagination
     * @param limit The maximum number of items to return
     * @return AppResult containing either the search results or an error message
     */
    suspend fun search(
        query: String,
        offset: Int,
        limit: Int
    ): AppResult<ProductSearchResult>
}