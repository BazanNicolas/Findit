package com.products.app.domain.usecase

import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving recently viewed products from the local database.
 * 
 * This use case handles the business logic for fetching recently viewed
 * products in chronological order (most recent first). It returns a Flow
 * to provide reactive updates when the viewed products list changes.
 * 
 * The use case supports pagination through the limit parameter, allowing
 * the presentation layer to control how many items to load at once.
 * 
 * @param viewedProductRepository The ViewedProductRepository for data access
 */
class GetRecentViewedProductsUseCase @Inject constructor(
    private val viewedProductRepository: ViewedProductRepository
) {
    /**
     * Executes the get recent viewed products operation.
     * 
     * This method retrieves the most recently viewed products from the
     * local database, ordered by timestamp (most recent first).
     * 
     * @param limit The maximum number of viewed products to return (default: 10)
     * @return Flow containing the list of recently viewed products
     */
    operator fun invoke(limit: Int = 10): Flow<List<ViewedProduct>> {
        return viewedProductRepository.getRecentViewedProducts(limit)
    }
}
