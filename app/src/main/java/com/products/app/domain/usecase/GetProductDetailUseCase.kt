package com.products.app.domain.usecase

import com.products.app.domain.model.ProductDetail
import com.products.app.domain.repository.ProductDetailRepository
import javax.inject.Inject

/**
 * Use case for retrieving detailed product information.
 * 
 * This use case handles the business logic for fetching comprehensive
 * product details from the MercadoLibre API. It delegates to the
 * ProductDetailRepository to retrieve detailed product information
 * including images, attributes, features, and specifications.
 * 
 * @param productDetailRepository The ProductDetailRepository for data access
 */
class GetProductDetailUseCase @Inject constructor(
    private val productDetailRepository: ProductDetailRepository
) {
    /**
     * Executes the get product detail operation with the given product ID.
     * 
     * @param productId The unique identifier of the product
     * @return Result containing either the detailed product information or an error
     */
    suspend operator fun invoke(productId: String): Result<ProductDetail> {
        return productDetailRepository.getProductDetail(productId)
    }
}
