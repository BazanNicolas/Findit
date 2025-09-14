package com.products.app.presentation.productDetail

import com.products.app.domain.model.ProductDetail

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val productDetail: ProductDetail? = null,
    val error: String? = null
)
