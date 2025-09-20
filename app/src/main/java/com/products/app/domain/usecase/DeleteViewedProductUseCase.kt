package com.products.app.domain.usecase

import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
import javax.inject.Inject

class DeleteViewedProductUseCase @Inject constructor(
    private val viewedProductRepository: ViewedProductRepository
) {
    suspend operator fun invoke(viewedProduct: ViewedProduct) {
        viewedProductRepository.deleteViewedProduct(viewedProduct)
    }
}
