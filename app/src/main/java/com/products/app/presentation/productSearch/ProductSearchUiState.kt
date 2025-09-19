package com.products.app.presentation.productSearch

import com.products.app.domain.model.Paging
import com.products.app.domain.model.Product

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