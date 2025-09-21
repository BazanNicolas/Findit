package com.products.app.domain.usecase

import com.products.app.domain.model.Product
import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
import javax.inject.Inject

/**
 * Use case for saving a viewed product to the local database.
 * 
 * This use case handles the business logic for persisting viewed products
 * to the local storage. It transforms a Product domain model into a
 * ViewedProduct entity and saves it with the current timestamp.
 * 
 * The use case automatically extracts relevant product information (ID, name,
 * thumbnail URL) and creates a ViewedProduct record with the current timestamp
 * for tracking when the product was viewed.
 * 
 * @param viewedProductRepository The ViewedProductRepository for data persistence
 */
class SaveViewedProductUseCase @Inject constructor(
    private val viewedProductRepository: ViewedProductRepository
) {
    /**
     * Executes the save viewed product operation.
     * 
     * This method creates a ViewedProduct entity from the provided Product
     * and saves it to the local database with the current timestamp.
     * 
     * @param product The Product that was viewed by the user
     */
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
