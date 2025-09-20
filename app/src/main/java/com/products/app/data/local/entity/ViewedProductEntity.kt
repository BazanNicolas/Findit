package com.products.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a viewed product in the local database.
 * 
 * This entity stores information about products that the user has viewed,
 * enabling the recently viewed products feature. The productId serves as
 * the primary key to prevent duplicate entries for the same product.
 * 
 * @property productId Unique identifier of the viewed product (Primary Key)
 * @property productName Name of the viewed product
 * @property thumbnailUrl URL of the product thumbnail image
 * @property timestamp When the product was viewed (Unix timestamp)
 */
@Entity(tableName = "viewed_products")
data class ViewedProductEntity(
    @PrimaryKey
    val productId: String,
    val productName: String,
    val thumbnailUrl: String?,
    val timestamp: Long
)
