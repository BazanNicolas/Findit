package com.products.app.domain.model

data class ProductDetail(
    val id: String,
    val catalogProductId: String?,
    val status: String,
    val pdpTypes: List<String>?,
    val domainId: String?,
    val permalink: String?,
    val name: String,
    val familyName: String?,
    val type: String?,
    val buyBoxWinner: String?,
    val pickers: List<ProductPicker>?,
    val pictures: List<ProductPicture>?,
    val descriptionPictures: List<ProductPicture>?,
    val mainFeatures: List<ProductFeature>?,
    val disclaimers: List<ProductDisclaimer>?,
    val attributes: List<ProductDetailAttribute>?,
    val shortDescription: ProductDescription?,
    val parentId: String?,
    val userProduct: String?,
    val childrenIds: List<String>?,
    val settings: ProductDetailSettings?,
    val qualityType: String?,
    val releaseInfo: String?,
    val presaleInfo: String?,
    val enhancedContent: String?,
    val tags: List<String>?,
    val dateCreated: String?,
    val authorizedStores: String?,
    val lastUpdated: String?,
    val grouperId: String?,
    val experiments: Map<String, Any>?
)

data class ProductPicker(
    val pickerId: String,
    val pickerName: String,
    val products: List<PickerProduct>?,
    val tags: List<String>?,
    val attributes: List<PickerAttribute>?,
    val valueNameDelimiter: String?
)

data class PickerProduct(
    val productId: String,
    val pickerLabel: String,
    val pictureId: String?,
    val thumbnail: String?,
    val tags: List<String>?,
    val permalink: String?,
    val productName: String,
    val autoCompleted: Boolean?
)

data class PickerAttribute(
    val attributeId: String,
    val template: String?
)

data class ProductPicture(
    val id: String,
    val url: String,
    val suggestedForPicker: List<String>?,
    val maxWidth: Int?,
    val maxHeight: Int?,
    val sourceMetadata: String?,
    val tags: List<String>?
)

data class ProductFeature(
    val text: String,
    val type: String?,
    val metadata: Map<String, Any>?
)

data class ProductDisclaimer(
    val text: String?,
    val type: String?,
    val metadata: Map<String, Any>?
)

data class ProductDetailAttribute(
    val id: String,
    val name: String,
    val valueId: String?,
    val valueName: String?,
    val values: List<ProductDetailAttributeValue>?,
    val meta: Map<String, Any>?
)

data class ProductDetailAttributeValue(
    val id: String?,
    val name: String?,
    val meta: Map<String, Any>?
)

data class ProductDescription(
    val type: String?,
    val content: String?
)

data class ProductDetailSettings(
    val content: String?,
    val listingStrategy: String?,
    val withEnhancedPictures: Boolean?,
    val baseSiteProductId: String?,
    val exclusive: Boolean?
)
