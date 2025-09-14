package com.products.app.data.mapper

import com.products.app.data.remote.dto.*
import com.products.app.domain.model.*

fun ProductDetailDto.toDomain(): ProductDetail {
    return ProductDetail(
        id = id,
        catalogProductId = catalog_product_id,
        status = status,
        pdpTypes = pdp_types,
        domainId = domain_id,
        permalink = permalink,
        name = name,
        familyName = family_name,
        type = type,
        buyBoxWinner = buy_box_winner,
        pickers = pickers?.map { it.toDomain() },
        pictures = pictures?.map { it.toDomain() },
        descriptionPictures = description_pictures?.map { it.toDomain() },
        mainFeatures = main_features?.map { it.toDomain() },
        disclaimers = disclaimers?.map { it.toDomain() },
        attributes = attributes?.map { it.toDomain() },
        shortDescription = short_description?.toDomain(),
        parentId = parent_id,
        userProduct = user_product,
        childrenIds = children_ids,
        settings = settings?.toDomain(),
        qualityType = quality_type,
        releaseInfo = release_info,
        presaleInfo = presale_info,
        enhancedContent = enhanced_content,
        tags = tags,
        dateCreated = date_created,
        authorizedStores = authorized_stores,
        lastUpdated = last_updated,
        grouperId = grouper_id,
        experiments = experiments
    )
}

fun ProductPickerDto.toDomain(): ProductPicker {
    return ProductPicker(
        pickerId = picker_id,
        pickerName = picker_name,
        products = products?.map { it.toDomain() },
        tags = tags,
        attributes = attributes?.map { it.toDomain() },
        valueNameDelimiter = value_name_delimiter
    )
}

fun PickerProductDto.toDomain(): PickerProduct {
    return PickerProduct(
        productId = product_id,
        pickerLabel = picker_label,
        pictureId = picture_id,
        thumbnail = thumbnail,
        tags = tags,
        permalink = permalink,
        productName = product_name,
        autoCompleted = auto_completed
    )
}

fun PickerAttributeDto.toDomain(): PickerAttribute {
    return PickerAttribute(
        attributeId = attribute_id,
        template = template
    )
}

fun ProductPictureDto.toDomain(): ProductPicture {
    return ProductPicture(
        id = id,
        url = url,
        suggestedForPicker = suggested_for_picker,
        maxWidth = max_width,
        maxHeight = max_height,
        sourceMetadata = source_metadata,
        tags = tags
    )
}

fun ProductFeatureDto.toDomain(): ProductFeature {
    return ProductFeature(
        text = text,
        type = type,
        metadata = metadata
    )
}

fun ProductDisclaimerDto.toDomain(): ProductDisclaimer {
    return ProductDisclaimer(
        text = text,
        type = type,
        metadata = metadata
    )
}

fun ProductDetailAttributeDto.toDomain(): ProductDetailAttribute {
    return ProductDetailAttribute(
        id = id,
        name = name,
        valueId = value_id,
        valueName = value_name,
        values = values?.map { it.toDomain() },
        meta = meta
    )
}

fun ProductDetailAttributeValueDto.toDomain(): ProductDetailAttributeValue {
    return ProductDetailAttributeValue(
        id = id,
        name = name,
        meta = meta
    )
}

fun ProductDescriptionDto.toDomain(): ProductDescription {
    return ProductDescription(
        type = type,
        content = content
    )
}

fun ProductDetailSettingsDto.toDomain(): ProductDetailSettings {
    return ProductDetailSettings(
        content = content,
        listingStrategy = listing_strategy,
        withEnhancedPictures = with_enhanced_pictures,
        baseSiteProductId = base_site_product_id,
        exclusive = exclusive
    )
}
