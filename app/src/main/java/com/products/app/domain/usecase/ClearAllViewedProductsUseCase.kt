package com.products.app.domain.usecase

import com.products.app.domain.repository.ViewedProductRepository
import javax.inject.Inject

class ClearAllViewedProductsUseCase @Inject constructor(
    private val viewedProductRepository: ViewedProductRepository
) {
    suspend operator fun invoke() {
        viewedProductRepository.clearAllViewedProducts()
    }
}
