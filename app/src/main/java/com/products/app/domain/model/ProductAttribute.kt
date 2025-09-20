package com.products.app.domain.model

/**
 * Represents a product attribute (e.g., brand, color, size, material).
 * 
 * This data class contains information about product characteristics that
 * can be used for filtering, searching, and displaying product details.
 * 
 * @property id Unique identifier for the attribute
 * @property name Display name of the attribute (e.g., "Brand", "Color")
 * @property valueId Unique identifier for the attribute value
 * @property valueName Display name of the attribute value (e.g., "Nike", "Red")
 */
data class ProductAttribute(
    val id: String?,
    val name: String?,
    val valueId: String?,
    val valueName: String?
)