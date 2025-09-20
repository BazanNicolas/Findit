package com.products.app.data.repository

import com.products.app.core.AppResult
import com.products.app.core.NetworkErrorHandler
import com.products.app.data.mapper.toDomain
import com.products.app.data.remote.ProductsApi
import com.products.app.domain.model.Paging
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
import jakarta.inject.Inject

/**
 * Implementation of ProductsRepository for handling product search operations.
 * 
 * This repository coordinates between the remote data source (MercadoLibre API)
 * and the domain layer, providing a clean abstraction for product search functionality.
 * It handles network errors gracefully using NetworkErrorHandler and converts
 * API responses to domain models.
 * 
 * The repository follows the Repository pattern, encapsulating data access logic
 * and providing a consistent interface for the domain layer regardless of the
 * underlying data source implementation.
 * 
 * @param api The Retrofit API interface for MercadoLibre products endpoint
 * @param errorHandler The NetworkErrorHandler for processing and localizing errors
 */
class ProductsRepositoryImpl @Inject constructor(
    private val api: ProductsApi,
    private val errorHandler: NetworkErrorHandler
) : ProductsRepository {

    /**
     * Searches for products using the MercadoLibre API.
     * 
     * This method performs a product search with pagination support. It calls the
     * MercadoLibre API with the specified search parameters and converts the response
     * to domain models. Network errors are caught and converted to user-friendly
     * localized messages.
     * 
     * @param query The search term to look for products
     * @param offset Number of items to skip for pagination (0-based)
     * @param limit Maximum number of items to return per page
     * @return AppResult containing either the search results or an error message
     */
    override suspend fun search(
        query: String,
        offset: Int,
        limit: Int
    ): AppResult<ProductSearchResult> = try {
        val dto = api.searchProducts(
            query = query,
            siteId = "MLA", // MercadoLibre Argentina
            status = "active", // Only active products
            offset = offset,
            limit = limit
        )
        AppResult.Success(dto.toDomain())
    } catch (e: Exception) {
        AppResult.Error(errorHandler.handleError(e))
    }
}