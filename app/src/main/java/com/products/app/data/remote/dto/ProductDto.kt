package com.products.app.data.remote.dto
data class ProductDto(
    val id: String,
    val status: String?,
    val domainId: String?,
    val name: String?,
    val permalink: String?,
    val mainFeatures: List<MainFeatureDto>?,
    val attributes: List<AttributeDto>?,
    val shortDescription: ShortDescriptionDto?,
    val parentId: String?,
    val childrenIds: List<String>?,
    val settings: SettingsDto?,
    val pictures: List<PictureDto>?,
    val buyBoxWinner: BuyBoxWinnerDto?
)
data class MainFeatureDto(
    val id: String?,
    val value: String?
)

data class AttributeDto(
    val id: String?,
    val name: String?,
    val valueId: String?,
    val valueName: String?,
    val values: List<SimpleIdName>?
)

data class ShortDescriptionDto(
    val type: String?,
    val content: String?
)

data class SettingsDto(
    val listingStrategy: String?
)

data class PictureDto(
    val id: String?,
    val url: String?
)

data class SimpleIdName(
    val id: String?,
    val name: String?
)
