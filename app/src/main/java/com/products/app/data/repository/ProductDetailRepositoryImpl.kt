package com.products.app.data.repository

import com.products.app.core.AppResult
import com.products.app.core.NetworkErrorHandler
import com.products.app.data.mapper.toDomain
import com.products.app.data.remote.ProductsApi
import com.products.app.domain.model.ProductDetail
import com.products.app.domain.repository.ProductDetailRepository
import javax.inject.Inject

/**
 * Implementation of ProductDetailRepository for handling detailed product information.
 * 
 * This repository provides access to comprehensive product details from the MercadoLibre API,
 * including images, attributes, features, and specifications. It serves as the data access
 * layer for product detail screens, handling network communication and error processing.
 * 
 * The repository follows the Repository pattern, providing a clean abstraction between
 * the domain layer and the remote data source. It handles API responses and converts
 * them to domain models while managing network errors gracefully.
 * 
 * @param productsApi The Retrofit API interface for MercadoLibre products endpoint
 * @param errorHandler The NetworkErrorHandler for processing and localizing errors
 */
class ProductDetailRepositoryImpl @Inject constructor(
    private val productsApi: ProductsApi,
    private val errorHandler: NetworkErrorHandler
) : ProductDetailRepository {
    
    /**
     * Retrieves detailed information for a specific product.
     * 
     * This method fetches comprehensive product details from the MercadoLibre API
     * using the provided product ID. It returns detailed information including
     * product images, attributes, features, descriptions, and specifications.
     * 
     * The method handles network errors gracefully and converts API responses
     * to domain models for consistent data handling throughout the application.
     * 
     * @param productId The unique identifier of the product to retrieve details for
     * @return AppResult containing either the detailed product information or an error message
     */
    override suspend fun getProductDetail(productId: String): AppResult<ProductDetail> {
        return try {
            val response = productsApi.getProductDetail(productId)
            AppResult.Success(response.toDomain())
        } catch (e: Exception) {
            AppResult.Error(errorHandler.handleError(e))
        }
    }
}
