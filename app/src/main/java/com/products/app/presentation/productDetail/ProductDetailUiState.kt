package com.products.app.presentation.productDetail

import com.products.app.domain.model.ProductDetail

/**
 * UI state for the Product Detail screen.
 * 
 * This data class represents the complete state of the product detail screen UI,
 * including loading states, product information, and error handling. It follows
 * the unidirectional data flow pattern where the UI state is immutable and
 * updated through ViewModel actions.
 * 
 * @property isLoading Whether the screen is in a loading state while fetching product details
 * @property productDetail The detailed product information (null if not loaded or error occurred)
 * @property error Error message to display to the user (null if no error)
 */
data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val productDetail: ProductDetail? = null,
    val error: String? = null
)
