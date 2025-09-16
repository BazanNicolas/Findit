package com.products.app.domain.usecase

import com.products.app.domain.model.Product
import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
import javax.inject.Inject

class SaveViewedProductUseCase @Inject constructor(
    private val viewedProductRepository: ViewedProductRepository
) {
    suspend operator fun invoke(product: Product) {
        val viewedProduct = ViewedProduct(
            productId = product.id,
            productName = product.name,
            thumbnailUrl = product.thumbnailUrl,
            timestamp = System.currentTimeMillis()
        )
        viewedProductRepository.saveViewedProduct(viewedProduct)
    }
}
