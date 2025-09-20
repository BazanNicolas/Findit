package com.products.app.presentation.productSearch

import com.products.app.domain.model.Paging
import com.products.app.domain.model.Product

/**
 * UI state for the Product Search screen.
 * 
 * This data class represents the complete state of the product search screen UI,
 * including search results, pagination, loading states, and error handling.
 * It supports infinite scrolling with pagination and handles both initial loading
 * and loading more results scenarios.
 * 
 * @property query The search query being displayed
 * @property loading Whether the initial search is in progress
 * @property loadingMore Whether more results are being loaded (pagination)
 * @property error Error message for the main search operation (null if no error)
 * @property paginationError Error message for pagination operations (null if no error)
 * @property products List of products from the search results
 * @property paging Pagination information for loading more results
 * @property hasReachedEnd Whether all available results have been loaded
 * @property isInitialLoad Whether this is the first load of search results
 */
data class ProductSearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val error: String? = null,
    val paginationError: String? = null,
    val products: List<Product> = emptyList(),
    val paging: Paging? = null,
    val hasReachedEnd: Boolean = false,
    val isInitialLoad: Boolean = true
)