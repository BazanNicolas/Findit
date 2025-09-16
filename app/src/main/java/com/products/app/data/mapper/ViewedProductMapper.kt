package com.products.app.data.mapper

import com.products.app.data.local.entity.ViewedProductEntity
import com.products.app.domain.model.ViewedProduct

fun ViewedProductEntity.toDomain(): ViewedProduct {
    return ViewedProduct(
        productId = productId,
        productName = productName,
        thumbnailUrl = thumbnailUrl,
        timestamp = timestamp
    )
}

fun ViewedProduct.toEntity(): ViewedProductEntity {
    return ViewedProductEntity(
        productId = productId,
        productName = productName,
        thumbnailUrl = thumbnailUrl,
        timestamp = timestamp
    )
}
