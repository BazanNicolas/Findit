package com.products.app.domain.usecase

import com.products.app.domain.repository.ViewedProductRepository
import javax.inject.Inject

/**
 * Use case for clearing all recently viewed products from the local database.
 * 
 * This use case handles the business logic for removing all viewed product
 * entries from the local storage. It's typically used when the user wants
 * to clear their viewing history completely.
 * 
 * The operation is irreversible and will remove all viewed product records
 * from the local database, effectively resetting the user's viewing history.
 * 
 * @param viewedProductRepository The ViewedProductRepository for data persistence
 */
class ClearAllViewedProductsUseCase @Inject constructor(
    private val viewedProductRepository: ViewedProductRepository
) {
    /**
     * Executes the clear all viewed products operation.
     * 
     * This method removes all viewed product entries from the local database,
     * effectively clearing the user's complete viewing history.
     */
    suspend operator fun invoke() {
        viewedProductRepository.clearAllViewedProducts()
    }
}
