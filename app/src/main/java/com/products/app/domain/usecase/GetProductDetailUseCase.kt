package com.products.app.domain.usecase

import com.products.app.domain.model.ProductDetail
import com.products.app.domain.repository.ProductDetailRepository
import javax.inject.Inject

class GetProductDetailUseCase @Inject constructor(
    private val productDetailRepository: ProductDetailRepository
) {
    suspend operator fun invoke(productId: String): Result<ProductDetail> {
        return productDetailRepository.getProductDetail(productId)
    }
}
