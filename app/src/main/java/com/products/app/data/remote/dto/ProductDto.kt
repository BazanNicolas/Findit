package com.products.app.data.remote.dto

import com.squareup.moshi.Json

data class ProductDto(
    val id: String,
    val status: String?,
    @field:Json(name = "domain_id") val domainId: String?,
    val name: String?,
    val permalink: String?,
    @field:Json(name = "main_features") val mainFeatures: List<MainFeatureDto>?,
    val attributes: List<AttributeDto>?,
    @field:Json(name = "short_description") val shortDescription: ShortDescriptionDto?,
    @field:Json(name = "parent_id") val parentId: String?,
    @field:Json(name = "children_ids") val childrenIds: List<String>?,
    val settings: SettingsDto?,
    val pictures: List<PictureDto>?,
    @field:Json(name = "buy_box_winner") val buyBoxWinner: BuyBoxWinnerDto?
)
data class MainFeatureDto(
    val id: String?,
    val value: String?
)

data class AttributeDto(
    val id: String?,
    val name: String?,
    @field:Json(name = "value_id") val valueId: String?,
    @field:Json(name = "value_name") val valueName: String?,
    val values: List<SimpleIdName>?
)

data class ShortDescriptionDto(
    val type: String?,
    val content: String?
)

data class SettingsDto(
    @field:Json(name = "listing_strategy") val listingStrategy: String?
)

data class PictureDto(
    val id: String?,
    val url: String?
)

data class SimpleIdName(
    val id: String?,
    val name: String?
)
