package com.products.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.products.app.data.remote.dto.*
import com.products.app.domain.model.ProductStatus
import com.products.app.util.MockDataFactory
import org.junit.Test

class ProductMapperTest {

    @Test
    fun `when ProductDto has all fields, should map to complete Product domain model`() {

        val productDto = MockDataFactory.createProductDto()
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.id).isEqualTo(productDto.id)
        assertThat(domainProduct.name).isEqualTo(productDto.name)
        assertThat(domainProduct.status).isEqualTo(ProductStatus.ACTIVE)
        assertThat(domainProduct.domainId).isEqualTo(productDto.domain_id)
        assertThat(domainProduct.permalink).isEqualTo(productDto.permalink)
        assertThat(domainProduct.catalogProductId).isEqualTo(productDto.catalog_product_id)
        assertThat(domainProduct.qualityType).isEqualTo(productDto.quality_type)
        assertThat(domainProduct.type).isEqualTo(productDto.type)
        assertThat(domainProduct.keywords).isEqualTo(productDto.keywords)
        assertThat(domainProduct.siteId).isEqualTo(productDto.site_id)
    }

    @Test
    fun `when ProductDto has active status, should map to ACTIVE enum`() {

        val productDto = MockDataFactory.createProductDto().copy(status = "active")
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.status).isEqualTo(ProductStatus.ACTIVE)
    }

    @Test
    fun `when ProductDto has inactive status, should map to INACTIVE enum`() {

        val productDto = MockDataFactory.createProductDto().copy(status = "inactive")
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.status).isEqualTo(ProductStatus.INACTIVE)
    }

    @Test
    fun `when ProductDto has unknown status, should map to UNKNOWN enum`() {

        val productDto = MockDataFactory.createProductDto().copy(status = "some_unknown_status")
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.status).isEqualTo(ProductStatus.UNKNOWN)
    }

    @Test
    fun `when ProductDto has null status, should map to UNKNOWN enum`() {

        val productDto = MockDataFactory.createProductDto().copy(status = null)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.status).isEqualTo(ProductStatus.UNKNOWN)
    }

    @Test
    fun `when ProductDto has pictures, should map thumbnail and picture URLs correctly`() {

        val pictures = listOf(
            PictureDto(id = "pic1", url = "https://example.com/pic1.jpg"),
            PictureDto(id = "pic2", url = "https://example.com/pic2.jpg"),
            PictureDto(id = "pic3", url = null) // null URL should be filtered out
        )
        val productDto = MockDataFactory.createProductDto().copy(pictures = pictures)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.thumbnailUrl).isEqualTo("https://example.com/pic1.jpg")
        assertThat(domainProduct.pictureUrls).hasSize(2)
        assertThat(domainProduct.pictureUrls).containsExactly(
            "https://example.com/pic1.jpg",
            "https://example.com/pic2.jpg"
        )
    }

    @Test
    fun `when ProductDto has no pictures, should have null thumbnail and empty picture URLs`() {

        val productDto = MockDataFactory.createProductDto().copy(pictures = emptyList())
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.thumbnailUrl).isNull()
        assertThat(domainProduct.pictureUrls).isEmpty()
    }

    @Test
    fun `when ProductDto has null pictures, should handle gracefully`() {

        val productDto = MockDataFactory.createProductDto().copy(pictures = null)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.thumbnailUrl).isNull()
        assertThat(domainProduct.pictureUrls).isEmpty()
    }

    @Test
    fun `when ProductDto has attributes, should map all attributes correctly`() {

        val attributes = listOf(
            AttributeDto(
                id = "BRAND",
                name = "Marca",
                value_id = "15996",
                value_name = "Apple",
                values = null,
                meta = null
            ),
            AttributeDto(
                id = "MODEL",
                name = "Modelo",
                value_id = "12345",
                value_name = "iPhone 15",
                values = null,
                meta = null
            )
        )
        val productDto = MockDataFactory.createProductDto().copy(attributes = attributes)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.attributes).hasSize(2)
        
        val brandAttribute = domainProduct.attributes[0]
        assertThat(brandAttribute.id).isEqualTo("BRAND")
        assertThat(brandAttribute.name).isEqualTo("Marca")
        assertThat(brandAttribute.valueId).isEqualTo("15996")
        assertThat(brandAttribute.valueName).isEqualTo("Apple")
        
        val modelAttribute = domainProduct.attributes[1]
        assertThat(modelAttribute.id).isEqualTo("MODEL")
        assertThat(modelAttribute.name).isEqualTo("Modelo")
        assertThat(modelAttribute.valueId).isEqualTo("12345")
        assertThat(modelAttribute.valueName).isEqualTo("iPhone 15")
    }

    @Test
    fun `when ProductDto has null attributes, should return empty attributes list`() {

        val productDto = MockDataFactory.createProductDto().copy(attributes = null)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.attributes).isEmpty()
    }

    @Test
    fun `when ProductDto has buy_box_winner with complete data, should map BuyBoxInfo correctly`() {

        val buyBoxWinner = BuyBoxWinnerDto(
            item_id = "MLA123456789",
            category_id = "MLA1055",
            price = 1299.99,
            currency_id = "USD",
            seller_id = 123456L,
            shipping = ShippingDto(mode = "me2")
        )
        val productDto = MockDataFactory.createProductDto().copy(buy_box_winner = buyBoxWinner)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.buyBox).isNotNull()
        assertThat(domainProduct.buyBox!!.price).isEqualTo(1299.99)
        assertThat(domainProduct.buyBox!!.currencyId).isEqualTo("USD")
        assertThat(domainProduct.buyBox!!.sellerId).isEqualTo(123456L)
    }

    @Test
    fun `when ProductDto has buy_box_winner with null price, should not map BuyBoxInfo`() {

        val buyBoxWinner = BuyBoxWinnerDto(
            item_id = "MLA123456789",
            category_id = "MLA1055",
            price = null,
            currency_id = "USD",
            seller_id = 123456L,
            shipping = ShippingDto(mode = "me2")
        )
        val productDto = MockDataFactory.createProductDto().copy(buy_box_winner = buyBoxWinner)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.buyBox).isNull()
    }

    @Test
    fun `when ProductDto has buy_box_winner with null currency, should not map BuyBoxInfo`() {

        val buyBoxWinner = BuyBoxWinnerDto(
            item_id = "MLA123456789",
            category_id = "MLA1055",
            price = 1299.99,
            currency_id = null,
            seller_id = 123456L,
            shipping = ShippingDto(mode = "me2")
        )
        val productDto = MockDataFactory.createProductDto().copy(buy_box_winner = buyBoxWinner)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.buyBox).isNull()
    }

    @Test
    fun `when ProductDto has children_ids, should set hasVariants to true`() {

        val productDto = MockDataFactory.createProductDto().copy(children_ids = listOf("child1", "child2"))
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.hasVariants).isTrue()
    }

    @Test
    fun `when ProductDto has no children_ids, should set hasVariants to false`() {

        val productDto = MockDataFactory.createProductDto().copy(children_ids = emptyList())
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.hasVariants).isFalse()
    }

    @Test
    fun `when ProductDto has null children_ids, should set hasVariants to false`() {

        val productDto = MockDataFactory.createProductDto().copy(children_ids = null)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.hasVariants).isFalse()
    }

    @Test
    fun `when ProductDto has null name, should map to empty string`() {

        val productDto = MockDataFactory.createProductDto().copy(name = null)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.name).isEmpty()
    }

    @Test
    fun `when ProductDto has blank permalink, should map to null`() {

        val productDto = MockDataFactory.createProductDto().copy(permalink = "")
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.permalink).isNull()
    }

    @Test
    fun `when ProductDto has blank short description, should map to null`() {

        val shortDescription = ShortDescriptionDto(type = "plain_text", content = "")
        val productDto = MockDataFactory.createProductDto().copy(short_description = shortDescription)
        val domainProduct = productDto.toDomain()
        assertThat(domainProduct.shortDescription).isNull()
    }

    @Test
    fun `when SearchResponseDto is mapped, should convert all fields correctly`() {

        val searchResponseDto = MockDataFactory.createSearchResponseDto()
        val domainResult = searchResponseDto.toDomain()
        assertThat(domainResult.products).hasSize(1)
        assertThat(domainResult.paging.total).isEqualTo(1000)
        assertThat(domainResult.paging.offset).isEqualTo(0)
        assertThat(domainResult.paging.limit).isEqualTo(10)
        assertThat(domainResult.keywords).isEqualTo("iphone")
        assertThat(domainResult.queryType).isEqualTo("search")
    }

    @Test
    fun `when PagingDto is mapped, should convert all fields correctly`() {

        val pagingDto = MockDataFactory.createPagingDto(
            total = 500,
            offset = 20,
            limit = 25
        )
        val domainPaging = pagingDto.toDomain()
        assertThat(domainPaging.total).isEqualTo(500)
        assertThat(domainPaging.offset).isEqualTo(20)
        assertThat(domainPaging.limit).isEqualTo(25)
    }
}
