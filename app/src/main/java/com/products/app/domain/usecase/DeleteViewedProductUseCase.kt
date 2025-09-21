package com.products.app.domain.usecase

import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
import javax.inject.Inject

/**
 * Use case for deleting a specific viewed product from the local database.
 * 
 * This use case handles the business logic for removing a single viewed product
 * entry from the local storage. It's typically used when the user wants to
 * remove a specific product from their viewing history.
 * 
 * The operation removes only the specified viewed product record from the
 * local database, leaving other viewed products intact.
 * 
 * @param viewedProductRepository The ViewedProductRepository for data persistence
 */
class DeleteViewedProductUseCase @Inject constructor(
    private val viewedProductRepository: ViewedProductRepository
) {
    /**
     * Executes the delete viewed product operation.
     * 
     * This method removes the specified viewed product entry from the local
     * database, effectively removing it from the user's viewing history.
     * 
     * @param viewedProduct The ViewedProduct to remove from the database
     */
    suspend operator fun invoke(viewedProduct: ViewedProduct) {
        viewedProductRepository.deleteViewedProduct(viewedProduct)
    }
}
