package com.products.app.domain.model

/**
 * Represents the availability status of a product.
 * 
 * This enum defines the possible states a product can have in the MercadoLibre marketplace.
 * It's used to filter and display products based on their availability.
 */
enum class ProductStatus { 
    ACTIVE,     // Product is available for purchase
    INACTIVE,   // Product is not available for purchase
    UNKNOWN     // Product status is not determined
}

/**
 * Represents a single product in the MercadoLibre marketplace.
 * 
 * This data class contains all the essential information about a product that is displayed
 * in search results and product listings. It includes basic product details, images,
 * attributes, and metadata needed for the UI.
 * 
 * @property id Unique identifier for the product
 * @property name Product name/title
 * @property status Current availability status of the product
 * @property domainId Domain identifier for the product category
 * @property permalink Direct URL to the product page
 * @property thumbnailUrl URL for the product thumbnail image
 * @property pictureUrls List of URLs for product images
 * @property attributes List of product attributes (size, color, brand, etc.)
 * @property shortDescription Brief description of the product
 * @property hasVariants Whether the product has multiple variants
 * @property buyBox Information about the best offer for this product
 * @property catalogProductId Catalog identifier for the product
 * @property qualityType Quality classification of the product
 * @property type Product type classification
 * @property keywords Search keywords associated with the product
 * @property siteId Site identifier (e.g., "MLA" for Argentina)
 */
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
    val buyBox: BuyBoxInfo?,
    val catalogProductId: String?,
    val qualityType: String?,
    val type: String?,
    val keywords: String?,
    val siteId: String?
)