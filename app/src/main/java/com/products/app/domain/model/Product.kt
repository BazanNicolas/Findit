package com.products.app.domain.model

enum class ProductStatus { ACTIVE, INACTIVE, UNKNOWN }

data class Product(
    val id: String,
    val name: String,
    val status: ProductStatus,
    val domainId: String?,
    val permalink: String?,
    val thumbnailUrl: String?,
    val pictureUrls: List<String>,
    val attributes: List<ProductAttribute>,
    val shortDescription: String?,
    val hasVariants: Boolean,
    val buyBox: BuyBoxInfo?
)