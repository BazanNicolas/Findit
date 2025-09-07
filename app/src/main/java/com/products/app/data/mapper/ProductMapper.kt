package com.products.app.data.mapper

import com.products.app.data.remote.dto.*
import com.products.app.domain.model.*

fun ProductDto.toDomain(): Product {
    val statusEnum = when (status?.lowercase()) {
        "active" -> ProductStatus.ACTIVE
        "inactive" -> ProductStatus.INACTIVE
        else -> ProductStatus.UNKNOWN
    }
    val pics = (pictures ?: emptyList()).mapNotNull { it.url }
    val buyBoxInfo = buyBoxWinner?.let {
        val price = it.price
        val currency = it.currencyId
        if (price != null && !currency.isNullOrBlank()) {
            BuyBoxInfo(price = price, currencyId = currency, sellerId = it.sellerId)
        } else null
    }

    return Product(
        id = id,
        name = name.orEmpty(),
        status = statusEnum,
        domainId = domainId,
        permalink = permalink?.ifBlank { null },
        thumbnailUrl = pics.firstOrNull(),
        pictureUrls = pics,
        attributes = (attributes ?: emptyList()).map {
            ProductAttribute(
                id = it.id,
                name = it.name,
                valueId = it.valueId,
                valueName = it.valueName
            )
        },
        shortDescription = shortDescription?.content?.ifBlank { null },
        hasVariants = !childrenIds.isNullOrEmpty(),
        buyBox = buyBoxInfo
    )
}