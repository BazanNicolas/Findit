package com.products.app.domain.model

data class ViewedProduct(
    val productId: String,
    val productName: String,
    val thumbnailUrl: String?,
    val timestamp: Long
)
