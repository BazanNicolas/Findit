package com.products.app.domain.model

data class ProductSearchResult(
    val products: List<Product>,
    val paging: Paging
)