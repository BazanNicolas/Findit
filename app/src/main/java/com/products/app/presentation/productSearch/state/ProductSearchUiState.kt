package com.products.app.presentation.productSearch.state

import com.products.app.domain.model.Paging
import com.products.app.domain.model.Product

data class ProductSearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val error: String? = null,
    val products: List<Product> = emptyList(),
    val paging: Paging? = null
)
