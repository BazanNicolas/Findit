package com.products.app.data.remote.dto

import com.squareup.moshi.Json

data class ProductDto(
    val id: String,
    val date_created: String?,
    val catalog_product_id: String?,
    val pdp_types: List<String>?,
    val status: String?,
    val domain_id: String?,
    val settings: SettingsDto?,
    val name: String?,
    val main_features: List<MainFeatureDto>?,
    val attributes: List<AttributeDto>?,
    val pictures: List<PictureDto>?,
    val parent_id: String?,
    val children_ids: List<String>?,
    val quality_type: String?,
    val priority: String?,
    val last_updated: String?,
    val type: String?,
    val keywords: String?,
    val site_id: String?,
    val variations: List<String>?,
    val buying_mode: String?,
    val channels: List<String>?,
    val description: String?,
    val permalink: String?,
    val short_description: ShortDescriptionDto?,
    val buy_box_winner: BuyBoxWinnerDto?
)
data class MainFeatureDto(
    val id: String?,
    val value: String?
)

data class AttributeDto(
    val id: String?,
    val name: String?,
    val value_id: String?,
    val value_name: String?,
    val values: List<AttributeValueDto>?,
    val meta: Map<String, Any>?
)

data class ShortDescriptionDto(
    val type: String?,
    val content: String?
)

data class SettingsDto(
    @field:Json(name = "listing_strategy") val listingStrategy: String?,
    val exclusive: Boolean?
)

data class PictureDto(
    val id: String?,
    val url: String?
)

data class AttributeValueDto(
    val id: String?,
    val name: String?,
    val meta: Map<String, Any>?
)
