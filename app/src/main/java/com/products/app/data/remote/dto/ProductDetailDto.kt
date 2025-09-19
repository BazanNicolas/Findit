package com.products.app.data.remote.dto

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class ProductDetailDto(
    val id: String,
    val catalog_product_id: String?,
    val status: String,
    val pdp_types: List<String>?,
    val domain_id: String?,
    val permalink: String?,
    val name: String,
    val family_name: String?,
    val type: String?,
    val buy_box_winner: String?,
    val pickers: List<ProductPickerDto>?,
    val pictures: List<ProductPictureDto>?,
    val description_pictures: List<ProductPictureDto>?,
    val main_features: List<ProductFeatureDto>?,
    val disclaimers: List<ProductDisclaimerDto>?,
    val attributes: List<ProductDetailAttributeDto>?,
    val short_description: ProductDescriptionDto?,
    val parent_id: String?,
    val user_product: String?,
    val children_ids: List<String>?,
    val settings: ProductDetailSettingsDto?,
    val quality_type: String?,
    val release_info: String?,
    val presale_info: String?,
    val enhanced_content: String?,
    val tags: List<String>?,
    val date_created: String?,
    val authorized_stores: String?,
    val last_updated: String?,
    val grouper_id: String?,
    val experiments: Map<String, Any>?
)

@JsonClass(generateAdapter = false)
data class ProductPickerDto(
    val picker_id: String,
    val picker_name: String,
    val products: List<PickerProductDto>?,
    val tags: List<String>?,
    val attributes: List<PickerAttributeDto>?,
    val value_name_delimiter: String?
)

@JsonClass(generateAdapter = false)
data class PickerProductDto(
    val product_id: String,
    val picker_label: String,
    val picture_id: String?,
    val thumbnail: String?,
    val tags: List<String>?,
    val permalink: String?,
    val product_name: String,
    val auto_completed: Boolean?
)

@JsonClass(generateAdapter = false)
data class PickerAttributeDto(
    val attribute_id: String,
    val template: String?
)

@JsonClass(generateAdapter = false)
data class ProductPictureDto(
    val id: String,
    val url: String,
    val suggested_for_picker: List<String>?,
    val max_width: Int?,
    val max_height: Int?,
    val source_metadata: String?,
    val tags: List<String>?
)

@JsonClass(generateAdapter = false)
data class ProductFeatureDto(
    val text: String,
    val type: String?,
    val metadata: Map<String, Any>?
)

@JsonClass(generateAdapter = false)
data class ProductDetailAttributeDto(
    val id: String,
    val name: String,
    val value_id: String?,
    val value_name: String?,
    val values: List<ProductDetailAttributeValueDto>?,
    val meta: Map<String, Any>?
)

@JsonClass(generateAdapter = false)
data class ProductDescriptionDto(
    val type: String?,
    val content: String?
)

@JsonClass(generateAdapter = false)
data class ProductDetailAttributeValueDto(
    val id: String?,
    val name: String?,
    val meta: Map<String, Any>?
)

@JsonClass(generateAdapter = false)
data class ProductDisclaimerDto(
    val text: String?,
    val type: String?,
    val metadata: Map<String, Any>?
)

@JsonClass(generateAdapter = false)
data class ProductDetailSettingsDto(
    val content: String?,
    val listing_strategy: String?,
    val with_enhanced_pictures: Boolean?,
    val base_site_product_id: String?,
    val exclusive: Boolean?
)
