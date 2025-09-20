package com.products.app.domain.model

/**
 * Represents detailed information about a product from MercadoLibre.
 * 
 * This data class contains comprehensive product information including images,
 * features, attributes, and metadata. It's used for displaying detailed product
 * information on the product detail screen.
 * 
 * @property id Unique identifier for the product
 * @property catalogProductId Catalog identifier for the product
 * @property status Current status of the product
 * @property pdpTypes Product detail page types
 * @property domainId Domain identifier for the product category
 * @property permalink Direct URL to the product page
 * @property name Product name/title
 * @property familyName Family name for grouped products
 * @property type Product type classification
 * @property buyBoxWinner Winner of the buy box competition
 * @property pickers Product pickers for variant selection
 * @property pictures List of product images
 * @property descriptionPictures Images used in product description
 * @property mainFeatures Key features of the product
 * @property disclaimers Legal disclaimers for the product
 * @property attributes Detailed product attributes
 * @property shortDescription Brief product description
 * @property parentId Parent product ID for variants
 * @property userProduct User-specific product information
 * @property childrenIds Child product IDs for grouped products
 * @property settings Product-specific settings
 * @property qualityType Quality classification
 * @property releaseInfo Information about product release
 * @property presaleInfo Presale information
 * @property enhancedContent Enhanced content for the product
 * @property tags Product tags for categorization
 * @property dateCreated Date when the product was created
 * @property authorizedStores Authorized stores for the product
 * @property lastUpdated Last update timestamp
 * @property grouperId Group identifier for related products
 * @property experiments A/B testing experiments data
 */
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

/**
 * Represents a product picker for variant selection.
 * 
 * Product pickers allow users to select different variants of a product
 * (e.g., size, color, model) and see the corresponding product options.
 * 
 * @property pickerId Unique identifier for the picker
 * @property pickerName Display name for the picker
 * @property products List of products available in this picker
 * @property tags Tags associated with the picker
 * @property attributes Attributes used in the picker
 * @property valueNameDelimiter Delimiter for value names
 */
data class ProductPicker(
    val pickerId: String,
    val pickerName: String,
    val products: List<PickerProduct>?,
    val tags: List<String>?,
    val attributes: List<PickerAttribute>?,
    val valueNameDelimiter: String?
)

/**
 * Represents a product option within a picker.
 * 
 * @property productId Unique identifier for the product
 * @property pickerLabel Label displayed in the picker
 * @property pictureId ID of the product picture
 * @property thumbnail URL of the product thumbnail
 * @property tags Tags associated with the product
 * @property permalink Direct URL to the product
 * @property productName Name of the product
 * @property autoCompleted Whether the product was auto-completed
 */
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

/**
 * Represents an attribute used in a product picker.
 * 
 * @property attributeId Unique identifier for the attribute
 * @property template Template used for displaying the attribute
 */
data class PickerAttribute(
    val attributeId: String,
    val template: String?
)

/**
 * Represents a product image with metadata.
 * 
 * @property id Unique identifier for the image
 * @property url URL of the image
 * @property suggestedForPicker List of pickers this image is suggested for
 * @property maxWidth Maximum width of the image
 * @property maxHeight Maximum height of the image
 * @property sourceMetadata Metadata about the image source
 * @property tags Tags associated with the image
 */
data class ProductPicture(
    val id: String,
    val url: String,
    val suggestedForPicker: List<String>?,
    val maxWidth: Int?,
    val maxHeight: Int?,
    val sourceMetadata: String?,
    val tags: List<String>?
)

/**
 * Represents a key feature of a product.
 * 
 * @property text Feature description text
 * @property type Type of feature (e.g., "highlight", "specification")
 * @property metadata Additional metadata for the feature
 */
data class ProductFeature(
    val text: String,
    val type: String?,
    val metadata: Map<String, Any>?
)

/**
 * Represents a legal disclaimer for a product.
 * 
 * @property text Disclaimer text content
 * @property type Type of disclaimer
 * @property metadata Additional metadata for the disclaimer
 */
data class ProductDisclaimer(
    val text: String?,
    val type: String?,
    val metadata: Map<String, Any>?
)

/**
 * Represents a detailed product attribute.
 * 
 * @property id Unique identifier for the attribute
 * @property name Display name of the attribute
 * @property valueId ID of the selected value
 * @property valueName Name of the selected value
 * @property values List of possible values for the attribute
 * @property meta Additional metadata for the attribute
 */
data class ProductDetailAttribute(
    val id: String,
    val name: String,
    val valueId: String?,
    val valueName: String?,
    val values: List<ProductDetailAttributeValue>?,
    val meta: Map<String, Any>?
)

/**
 * Represents a possible value for a product attribute.
 * 
 * @property id Unique identifier for the value
 * @property name Display name of the value
 * @property meta Additional metadata for the value
 */
data class ProductDetailAttributeValue(
    val id: String?,
    val name: String?,
    val meta: Map<String, Any>?
)

/**
 * Represents a product description with type information.
 * 
 * @property type Type of description (e.g., "plain", "html")
 * @property content Description content
 */
data class ProductDescription(
    val type: String?,
    val content: String?
)

/**
 * Represents product-specific settings and configurations.
 * 
 * @property content Settings content
 * @property listingStrategy Strategy used for product listing
 * @property withEnhancedPictures Whether enhanced pictures are enabled
 * @property baseSiteProductId Base site product identifier
 * @property exclusive Whether the product is exclusive
 */
data class ProductDetailSettings(
    val content: String?,
    val listingStrategy: String?,
    val withEnhancedPictures: Boolean?,
    val baseSiteProductId: String?,
    val exclusive: Boolean?
)
