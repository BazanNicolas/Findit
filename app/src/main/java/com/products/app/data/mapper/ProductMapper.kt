package com.products.app.data.mapper

import com.products.app.data.remote.dto.*
import com.products.app.domain.model.*

fun ProductDto.toDomain(): Product {
    val statusEnum = when (status?.lowercase()) {
        "active" -> ProductStatus.ACTIVE
        "inactive" -> ProductStatus.INACTIVE
        else -> ProductStatus.UNKNOWN
    }
    val pics = (pictures ?: emptyList()).mapNotNull { picture -> 
        picture.url?.takeIf { it.isNotBlank() }
    }
    val buyBoxInfo = buy_box_winner?.let {
        val price = it.price
        val currency = it.currency_id
        if (price != null && !currency.isNullOrBlank()) {
            BuyBoxInfo(price = price, currencyId = currency, sellerId = it.seller_id)
        } else null
    }

    return Product(
        id = id,
        name = name.orEmpty(),
        status = statusEnum,
        domainId = domain_id,
        permalink = permalink?.ifBlank { null },
        thumbnailUrl = pics.firstOrNull(),
        pictureUrls = pics,
        attributes = (attributes ?: emptyList()).map {
            ProductAttribute(
                id = it.id,
                name = it.name,
                valueId = it.value_id,
                valueName = it.value_name
            )
        },
        shortDescription = short_description?.content?.ifBlank { null },
        hasVariants = !children_ids.isNullOrEmpty(),
        buyBox = buyBoxInfo,
        catalogProductId = catalog_product_id,
        qualityType = quality_type,
        type = type,
        keywords = keywords,
        siteId = site_id
    )
}

fun SearchResponseDto.toDomain(): ProductSearchResult {
    return ProductSearchResult(
        products = results.map { it.toDomain() },
        paging = paging.toDomain(),
        keywords = keywords,
        usedAttributes = used_attributes?.map { it.toDomain() },
        queryType = query_type
    )
}

fun PagingDto.toDomain(): Paging {
    return Paging(
        total = total,
        limit = limit,
        offset = offset
    )
}

fun AttributeDto.toDomain(): ProductAttribute {
    return ProductAttribute(
        id = id,
        name = name,
        valueId = value_id,
        valueName = value_name
    )
}