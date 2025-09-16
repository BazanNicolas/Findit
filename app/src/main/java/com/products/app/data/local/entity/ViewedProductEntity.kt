package com.products.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viewed_products")
data class ViewedProductEntity(
    @PrimaryKey
    val productId: String,
    val productName: String,
    val thumbnailUrl: String?,
    val timestamp: Long
)
