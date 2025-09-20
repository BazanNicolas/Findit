package com.products.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.products.app.data.remote.dto.*
import com.products.app.domain.model.*
import org.junit.Test

class ProductDetailMapperTest {

    @Test
    fun `ProductDetailDto toDomain should map all fields correctly`() {

        val dto = ProductDetailDto(
            id = "MLA123456789",
            catalog_product_id = "MLA123456789",
            status = "active",
            pdp_types = listOf("regular", "premium"),
            domain_id = "MLA-CELLPHONES",
            permalink = "https://www.mercadolibre.com.ar/product/MLA123456789",
            name = "iPhone 15 Pro Max",
            family_name = "iPhone 15",
            type = "product",
            buy_box_winner = null,
            pickers = listOf(
                ProductPickerDto(
                    picker_id = "picker1",
                    picker_name = "Color",
                    products = listOf(
                        PickerProductDto(
                            product_id = "MLA111",
                            picker_label = "Blue",
                            picture_id = "pic1",
                            thumbnail = "thumb1.jpg",
                            tags = emptyList(),
                            permalink = "permalink1",
                            product_name = "iPhone Blue",
                            auto_completed = false
                        )
                    ),
                    tags = emptyList(),
                    attributes = listOf(
                        PickerAttributeDto(
                            attribute_id = "COLOR",
                            template = "color_template"
                        )
                    ),
                    value_name_delimiter = ","
                )
            ),
            pictures = listOf(
                ProductPictureDto(
                    id = "pic1",
                    url = "https://example.com/pic1.jpg",
                    suggested_for_picker = listOf("picker1"),
                    max_width = 1200,
                    max_height = 1200,
                    source_metadata = null,
                    tags = emptyList()
                )
            ),
            description_pictures = emptyList(),
            main_features = listOf(
                ProductFeatureDto(
                    text = "A17 Pro chip",
                    type = "feature",
                    metadata = emptyMap()
                )
            ),
            disclaimers = listOf(
                ProductDisclaimerDto(
                    text = "Warranty disclaimer",
                    type = "warranty",
                    metadata = emptyMap()
                )
            ),
            attributes = listOf(
                ProductDetailAttributeDto(
                    id = "BRAND",
                    name = "Marca",
                    value_id = "15996",
                    value_name = "Apple",
                    values = listOf(
                        ProductDetailAttributeValueDto(
                            id = "15996",
                            name = "Apple",
                            meta = emptyMap()
                        )
                    ),
                    meta = emptyMap()
                )
            ),
            short_description = ProductDescriptionDto(
                type = "plain_text",
                content = "El iPhone más avanzado"
            ),
            parent_id = null,
            user_product = null,
            children_ids = emptyList(),
            settings = ProductDetailSettingsDto(
                content = "premium",
                listing_strategy = "premium",
                with_enhanced_pictures = true,
                base_site_product_id = "MLA123456789",
                exclusive = false
            ),
            quality_type = "premium",
            release_info = null,
            presale_info = null,
            enhanced_content = null,
            tags = emptyList(),
            date_created = "2024-01-01T00:00:00.000Z",
            authorized_stores = null,
            last_updated = "2024-01-01T00:00:00.000Z",
            grouper_id = null,
            experiments = emptyMap()
        )
        val result = dto.toDomain()
        assertThat(result.id).isEqualTo("MLA123456789")
        assertThat(result.catalogProductId).isEqualTo("MLA123456789")
        assertThat(result.status).isEqualTo("active")
        assertThat(result.pdpTypes).isEqualTo(listOf("regular", "premium"))
        assertThat(result.domainId).isEqualTo("MLA-CELLPHONES")
        assertThat(result.permalink).isEqualTo("https://www.mercadolibre.com.ar/product/MLA123456789")
        assertThat(result.name).isEqualTo("iPhone 15 Pro Max")
        assertThat(result.familyName).isEqualTo("iPhone 15")
        assertThat(result.type).isEqualTo("product")
        assertThat(result.buyBoxWinner).isNull()
        assertThat(result.pickers).hasSize(1)
        assertThat(result.pictures).hasSize(1)
        assertThat(result.mainFeatures).hasSize(1)
        assertThat(result.disclaimers).hasSize(1)
        assertThat(result.attributes).hasSize(1)
        assertThat(result.shortDescription).isNotNull()
        assertThat(result.parentId).isNull()
        assertThat(result.userProduct).isNull()
        assertThat(result.childrenIds).isEmpty()
        assertThat(result.settings).isNotNull()
        assertThat(result.qualityType).isEqualTo("premium")
        assertThat(result.releaseInfo).isNull()
        assertThat(result.presaleInfo).isNull()
        assertThat(result.enhancedContent).isNull()
        assertThat(result.tags).isEmpty()
        assertThat(result.dateCreated).isEqualTo("2024-01-01T00:00:00.000Z")
        assertThat(result.authorizedStores).isNull()
        assertThat(result.lastUpdated).isEqualTo("2024-01-01T00:00:00.000Z")
        assertThat(result.grouperId).isNull()
        assertThat(result.experiments).isEmpty()
    }

    @Test
    fun `ProductDetailDto with null fields should map correctly`() {

        val dto = ProductDetailDto(
            id = "MLA123456789",
            catalog_product_id = "",
            status = "",
            pdp_types = null,
            domain_id = "",
            permalink = "",
            name = "",
            family_name = "",
            type = "",
            buy_box_winner = null,
            pickers = null,
            pictures = null,
            description_pictures = null,
            main_features = null,
            disclaimers = null,
            attributes = null,
            short_description = null,
            parent_id = "",
            user_product = null,
            children_ids = null,
            settings = null,
            quality_type = "",
            release_info = null,
            presale_info = null,
            enhanced_content = null,
            tags = null,
            date_created = "",
            authorized_stores = null,
            last_updated = "",
            grouper_id = "",
            experiments = null
        )
        val result = dto.toDomain()
        assertThat(result.id).isEqualTo("MLA123456789")
        assertThat(result.catalogProductId).isEmpty()
        assertThat(result.status).isEmpty()
        assertThat(result.pdpTypes).isNull()
        assertThat(result.domainId).isEmpty()
        assertThat(result.permalink).isEmpty()
        assertThat(result.name).isEmpty()
        assertThat(result.familyName).isEmpty()
        assertThat(result.type).isEmpty()
        assertThat(result.buyBoxWinner).isNull()
        assertThat(result.pickers).isNull()
        assertThat(result.pictures).isNull()
        assertThat(result.descriptionPictures).isNull()
        assertThat(result.mainFeatures).isNull()
        assertThat(result.disclaimers).isNull()
        assertThat(result.attributes).isNull()
        assertThat(result.shortDescription).isNull()
        assertThat(result.parentId).isEmpty()
        assertThat(result.userProduct).isNull()
        assertThat(result.childrenIds).isNull()
        assertThat(result.settings).isNull()
        assertThat(result.qualityType).isEmpty()
        assertThat(result.releaseInfo).isNull()
        assertThat(result.presaleInfo).isNull()
        assertThat(result.enhancedContent).isNull()
        assertThat(result.tags).isNull()
        assertThat(result.dateCreated).isEmpty()
        assertThat(result.authorizedStores).isNull()
        assertThat(result.lastUpdated).isEmpty()
        assertThat(result.grouperId).isEmpty()
        assertThat(result.experiments).isNull()
    }

    @Test
    fun `ProductPickerDto toDomain should map all fields correctly`() {

        val dto = ProductPickerDto(
            picker_id = "picker1",
            picker_name = "Color",
            products = listOf(
                PickerProductDto(
                    product_id = "MLA111",
                    picker_label = "Blue",
                    picture_id = "pic1",
                    thumbnail = "thumb1.jpg",
                    tags = emptyList(),
                    permalink = "permalink1",
                    product_name = "iPhone Blue",
                    auto_completed = false
                )
            ),
            tags = listOf("color", "variant"),
            attributes = listOf(
                PickerAttributeDto(
                    attribute_id = "COLOR",
                    template = "color_template"
                )
            ),
            value_name_delimiter = ","
        )
        val result = dto.toDomain()
        assertThat(result.pickerId).isEqualTo("picker1")
        assertThat(result.pickerName).isEqualTo("Color")
        assertThat(result.products).hasSize(1)
        assertThat(result.tags).isEqualTo(listOf("color", "variant"))
        assertThat(result.attributes).hasSize(1)
        assertThat(result.valueNameDelimiter).isEqualTo(",")
    }

    @Test
    fun `ProductPickerDto with null fields should map correctly`() {

        val dto = ProductPickerDto(
            picker_id = "picker1",
            picker_name = "Color",
            products = null,
            tags = null,
            attributes = null,
            value_name_delimiter = null
        )
        val result = dto.toDomain()
        assertThat(result.pickerId).isEqualTo("picker1")
        assertThat(result.pickerName).isEqualTo("Color")
        assertThat(result.products).isNull()
        assertThat(result.tags).isNull()
        assertThat(result.attributes).isNull()
        assertThat(result.valueNameDelimiter).isNull()
    }

    @Test
    fun `PickerProductDto toDomain should map all fields correctly`() {

        val dto = PickerProductDto(
            product_id = "MLA111",
            picker_label = "Blue",
            picture_id = "pic1",
            thumbnail = "thumb1.jpg",
            tags = listOf("blue", "color"),
            permalink = "permalink1",
            product_name = "iPhone Blue",
            auto_completed = true
        )
        val result = dto.toDomain()
        assertThat(result.productId).isEqualTo("MLA111")
        assertThat(result.pickerLabel).isEqualTo("Blue")
        assertThat(result.pictureId).isEqualTo("pic1")
        assertThat(result.thumbnail).isEqualTo("thumb1.jpg")
        assertThat(result.tags).isEqualTo(listOf("blue", "color"))
        assertThat(result.permalink).isEqualTo("permalink1")
        assertThat(result.productName).isEqualTo("iPhone Blue")
        assertThat(result.autoCompleted).isTrue()
    }

    @Test
    fun `PickerAttributeDto toDomain should map all fields correctly`() {

        val dto = PickerAttributeDto(
            attribute_id = "COLOR",
            template = "color_template"
        )
        val result = dto.toDomain()
        assertThat(result.attributeId).isEqualTo("COLOR")
        assertThat(result.template).isEqualTo("color_template")
    }

    @Test
    fun `ProductPictureDto toDomain should map all fields correctly`() {

        val dto = ProductPictureDto(
            id = "pic1",
            url = "https://example.com/pic1.jpg",
            suggested_for_picker = listOf("picker1", "picker2"),
            max_width = 1200,
            max_height = 800,
            source_metadata = "api",
            tags = listOf("main", "thumbnail")
        )
        val result = dto.toDomain()
        assertThat(result.id).isEqualTo("pic1")
        assertThat(result.url).isEqualTo("https://example.com/pic1.jpg")
        assertThat(result.suggestedForPicker).isEqualTo(listOf("picker1", "picker2"))
        assertThat(result.maxWidth).isEqualTo(1200)
        assertThat(result.maxHeight).isEqualTo(800)
        assertThat(result.sourceMetadata).isEqualTo("api")
        assertThat(result.tags).isEqualTo(listOf("main", "thumbnail"))
    }

    @Test
    fun `ProductFeatureDto toDomain should map all fields correctly`() {

        val dto = ProductFeatureDto(
            text = "A17 Pro chip",
            type = "feature",
            metadata = mapOf("category" to "processor")
        )
        val result = dto.toDomain()
        assertThat(result.text).isEqualTo("A17 Pro chip")
        assertThat(result.type).isEqualTo("feature")
        assertThat(result.metadata).isEqualTo(mapOf("category" to "processor"))
    }

    @Test
    fun `ProductDisclaimerDto toDomain should map all fields correctly`() {

        val dto = ProductDisclaimerDto(
            text = "Warranty disclaimer",
            type = "warranty",
            metadata = mapOf("duration" to "1 year")
        )
        val result = dto.toDomain()
        assertThat(result.text).isEqualTo("Warranty disclaimer")
        assertThat(result.type).isEqualTo("warranty")
        assertThat(result.metadata).isEqualTo(mapOf("duration" to "1 year"))
    }

    @Test
    fun `ProductDetailAttributeDto toDomain should map all fields correctly`() {

        val dto = ProductDetailAttributeDto(
            id = "BRAND",
            name = "Marca",
            value_id = "15996",
            value_name = "Apple",
            values = listOf(
                ProductDetailAttributeValueDto(
                    id = "15996",
                    name = "Apple",
                    meta = emptyMap()
                )
            ),
            meta = mapOf("category" to "brand")
        )
        val result = dto.toDomain()
        assertThat(result.id).isEqualTo("BRAND")
        assertThat(result.name).isEqualTo("Marca")
        assertThat(result.valueId).isEqualTo("15996")
        assertThat(result.valueName).isEqualTo("Apple")
        assertThat(result.values).hasSize(1)
        assertThat(result.meta).isEqualTo(mapOf("category" to "brand"))
    }

    @Test
    fun `ProductDetailAttributeValueDto toDomain should map all fields correctly`() {

        val dto = ProductDetailAttributeValueDto(
            id = "15996",
            name = "Apple",
            meta = mapOf("brand_id" to "15996")
        )
        val result = dto.toDomain()
        assertThat(result.id).isEqualTo("15996")
        assertThat(result.name).isEqualTo("Apple")
        assertThat(result.meta).isEqualTo(mapOf("brand_id" to "15996"))
    }

    @Test
    fun `ProductDescriptionDto toDomain should map all fields correctly`() {

        val dto = ProductDescriptionDto(
            type = "plain_text",
            content = "El iPhone más avanzado con chip A17 Pro"
        )
        val result = dto.toDomain()
        assertThat(result.type).isEqualTo("plain_text")
        assertThat(result.content).isEqualTo("El iPhone más avanzado con chip A17 Pro")
    }

    @Test
    fun `ProductDetailSettingsDto toDomain should map all fields correctly`() {

        val dto = ProductDetailSettingsDto(
            content = "premium",
            listing_strategy = "premium",
            with_enhanced_pictures = true,
            base_site_product_id = "MLA123456789",
            exclusive = false
        )
        val result = dto.toDomain()
        assertThat(result.content).isEqualTo("premium")
        assertThat(result.listingStrategy).isEqualTo("premium")
        assertThat(result.withEnhancedPictures).isTrue()
        assertThat(result.baseSiteProductId).isEqualTo("MLA123456789")
        assertThat(result.exclusive).isFalse()
    }

    @Test
    fun `empty lists should be mapped correctly`() {

        val dto = ProductDetailDto(
            id = "MLA123456789",
            catalog_product_id = "MLA123456789",
            status = "active",
            pdp_types = emptyList(),
            domain_id = "MLA-CELLPHONES",
            permalink = "https://example.com",
            name = "Test Product",
            family_name = "Test Family",
            type = "product",
            buy_box_winner = null,
            pickers = emptyList(),
            pictures = emptyList(),
            description_pictures = emptyList(),
            main_features = emptyList(),
            disclaimers = emptyList(),
            attributes = emptyList(),
            short_description = null,
            parent_id = null,
            user_product = null,
            children_ids = emptyList(),
            settings = null,
            quality_type = "premium",
            release_info = null,
            presale_info = null,
            enhanced_content = null,
            tags = emptyList(),
            date_created = null,
            authorized_stores = null,
            last_updated = null,
            grouper_id = null,
            experiments = emptyMap()
        )
        val result = dto.toDomain()
        assertThat(result.pdpTypes).isEmpty()
        assertThat(result.pickers).isEmpty()
        assertThat(result.pictures).isEmpty()
        assertThat(result.descriptionPictures).isEmpty()
        assertThat(result.mainFeatures).isEmpty()
        assertThat(result.disclaimers).isEmpty()
        assertThat(result.attributes).isEmpty()
        assertThat(result.childrenIds).isEmpty()
        assertThat(result.tags).isEmpty()
        assertThat(result.experiments).isEmpty()
    }
}
