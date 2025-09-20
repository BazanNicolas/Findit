package com.products.app.domain.model

/**
 * Represents a product that has been viewed by the user.
 * 
 * This data class stores information about products that the user has viewed,
 * allowing the app to show recently viewed products for quick access.
 * 
 * @property productId Unique identifier of the viewed product
 * @property productName Name of the viewed product
 * @property thumbnailUrl URL of the product thumbnail image
 * @property timestamp When the product was viewed (Unix timestamp)
 */
data class ViewedProduct(
    val productId: String,
    val productName: String,
    val thumbnailUrl: String?,
    val timestamp: Long
)
