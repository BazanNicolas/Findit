package com.products.app.domain.usecase

import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentViewedProductsUseCase @Inject constructor(
    private val viewedProductRepository: ViewedProductRepository
) {
    operator fun invoke(limit: Int = 10): Flow<List<ViewedProduct>> {
        return viewedProductRepository.getRecentViewedProducts(limit)
    }
}
