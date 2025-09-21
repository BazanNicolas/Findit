package com.products.app.domain.repository

import com.products.app.core.AppResult
import com.products.app.domain.model.ProductDetail

/**
 * Repository interface for managing product detail operations.
 * 
 * This interface defines the contract for product detail data access,
 * providing methods for retrieving comprehensive product information.
 * It abstracts the data source details and provides a clean interface
 * for the domain layer to interact with product detail functionality.
 * 
 * The repository pattern ensures separation of concerns and makes the
 * codebase more testable and maintainable.
 */
interface ProductDetailRepository {
    /**
     * Retrieves detailed information for a specific product.
     * 
     * @param productId The unique identifier of the product
     * @return AppResult containing either the detailed product information or an error message
     */
    suspend fun getProductDetail(productId: String): AppResult<ProductDetail>
}
