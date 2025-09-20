package com.products.app.data.mapper

import com.google.common.truth.Truth.assertThat
import com.products.app.data.local.entity.ViewedProductEntity
import com.products.app.domain.model.ViewedProduct
import org.junit.Test

class ViewedProductMapperTest {

    @Test
    fun `ViewedProductEntity toDomain should map all fields correctly`() {

        val timestamp = System.currentTimeMillis()
        val entity = ViewedProductEntity(
            productId = "MLA123456789",
            productName = "iPhone 15 Pro Max 256GB",
            thumbnailUrl = "https://http2.mlstatic.com/D_NQ_NP_123456-MLA70381330133_072023-I.webp",
            timestamp = timestamp
        )
        val result = entity.toDomain()
        assertThat(result.productId).isEqualTo("MLA123456789")
        assertThat(result.productName).isEqualTo("iPhone 15 Pro Max 256GB")
        assertThat(result.thumbnailUrl).isEqualTo("https://http2.mlstatic.com/D_NQ_NP_123456-MLA70381330133_072023-I.webp")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProductEntity with null thumbnail should map correctly`() {

        val timestamp = System.currentTimeMillis()
        val entity = ViewedProductEntity(
            productId = "MLA999999999",
            productName = "Product without thumbnail",
            thumbnailUrl = null,
            timestamp = timestamp
        )
        val result = entity.toDomain()
        assertThat(result.productId).isEqualTo("MLA999999999")
        assertThat(result.productName).isEqualTo("Product without thumbnail")
        assertThat(result.thumbnailUrl).isNull()
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProductEntity with empty product name should map correctly`() {

        val timestamp = System.currentTimeMillis()
        val entity = ViewedProductEntity(
            productId = "MLA888888888",
            productName = "",
            thumbnailUrl = "thumb.jpg",
            timestamp = timestamp
        )
        val result = entity.toDomain()
        assertThat(result.productId).isEqualTo("MLA888888888")
        assertThat(result.productName).isEmpty()
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProductEntity with empty product id should map correctly`() {

        val timestamp = System.currentTimeMillis()
        val entity = ViewedProductEntity(
            productId = "",
            productName = "Product with empty ID",
            thumbnailUrl = "thumb.jpg",
            timestamp = timestamp
        )
        val result = entity.toDomain()
        assertThat(result.productId).isEmpty()
        assertThat(result.productName).isEqualTo("Product with empty ID")
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProductEntity with zero timestamp should map correctly`() {

        val entity = ViewedProductEntity(
            productId = "MLA777777777",
            productName = "Product with zero timestamp",
            thumbnailUrl = "thumb.jpg",
            timestamp = 0L
        )
        val result = entity.toDomain()
        assertThat(result.productId).isEqualTo("MLA777777777")
        assertThat(result.productName).isEqualTo("Product with zero timestamp")
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(0L)
    }

    @Test
    fun `ViewedProductEntity with negative timestamp should map correctly`() {

        val entity = ViewedProductEntity(
            productId = "MLA666666666",
            productName = "Product with negative timestamp",
            thumbnailUrl = "thumb.jpg",
            timestamp = -1000L
        )
        val result = entity.toDomain()
        assertThat(result.productId).isEqualTo("MLA666666666")
        assertThat(result.productName).isEqualTo("Product with negative timestamp")
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(-1000L)
    }

    @Test
    fun `ViewedProduct toEntity should map all fields correctly`() {

        val timestamp = System.currentTimeMillis()
        val viewedProduct = ViewedProduct(
            productId = "MLA555555555",
            productName = "Samsung Galaxy S24 Ultra",
            thumbnailUrl = "https://http2.mlstatic.com/D_NQ_NP_555555-MLA555555555_555555-I.webp",
            timestamp = timestamp
        )
        val result = viewedProduct.toEntity()
        assertThat(result.productId).isEqualTo("MLA555555555")
        assertThat(result.productName).isEqualTo("Samsung Galaxy S24 Ultra")
        assertThat(result.thumbnailUrl).isEqualTo("https://http2.mlstatic.com/D_NQ_NP_555555-MLA555555555_555555-I.webp")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProduct with null thumbnail should map to entity correctly`() {

        val timestamp = System.currentTimeMillis()
        val viewedProduct = ViewedProduct(
            productId = "MLA444444444",
            productName = "Product without thumbnail",
            thumbnailUrl = null,
            timestamp = timestamp
        )
        val result = viewedProduct.toEntity()
        assertThat(result.productId).isEqualTo("MLA444444444")
        assertThat(result.productName).isEqualTo("Product without thumbnail")
        assertThat(result.thumbnailUrl).isNull()
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProduct with empty product name should map to entity correctly`() {

        val timestamp = System.currentTimeMillis()
        val viewedProduct = ViewedProduct(
            productId = "MLA333333333",
            productName = "",
            thumbnailUrl = "thumb.jpg",
            timestamp = timestamp
        )
        val result = viewedProduct.toEntity()
        assertThat(result.productId).isEqualTo("MLA333333333")
        assertThat(result.productName).isEmpty()
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProduct with empty product id should map to entity correctly`() {

        val timestamp = System.currentTimeMillis()
        val viewedProduct = ViewedProduct(
            productId = "",
            productName = "Product with empty ID",
            thumbnailUrl = "thumb.jpg",
            timestamp = timestamp
        )
        val result = viewedProduct.toEntity()
        assertThat(result.productId).isEmpty()
        assertThat(result.productName).isEqualTo("Product with empty ID")
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProduct with zero timestamp should map to entity correctly`() {

        val viewedProduct = ViewedProduct(
            productId = "MLA222222222",
            productName = "Product with zero timestamp",
            thumbnailUrl = "thumb.jpg",
            timestamp = 0L
        )
        val result = viewedProduct.toEntity()
        assertThat(result.productId).isEqualTo("MLA222222222")
        assertThat(result.productName).isEqualTo("Product with zero timestamp")
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(0L)
    }

    @Test
    fun `ViewedProduct with negative timestamp should map to entity correctly`() {

        val viewedProduct = ViewedProduct(
            productId = "MLA111111111",
            productName = "Product with negative timestamp",
            thumbnailUrl = "thumb.jpg",
            timestamp = -1000L
        )
        val result = viewedProduct.toEntity()
        assertThat(result.productId).isEqualTo("MLA111111111")
        assertThat(result.productName).isEqualTo("Product with negative timestamp")
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(-1000L)
    }

    @Test
    fun `ViewedProduct with very long product name should map correctly`() {

        val timestamp = System.currentTimeMillis()
        val longProductName = "This is a very long product name that might exceed normal limits and should be handled correctly by the mapper"
        val viewedProduct = ViewedProduct(
            productId = "MLA000000000",
            productName = longProductName,
            thumbnailUrl = "thumb.jpg",
            timestamp = timestamp
        )
        val result = viewedProduct.toEntity()
        assertThat(result.productId).isEqualTo("MLA000000000")
        assertThat(result.productName).isEqualTo(longProductName)
        assertThat(result.thumbnailUrl).isEqualTo("thumb.jpg")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `ViewedProduct with special characters should map correctly`() {

        val timestamp = System.currentTimeMillis()
        val viewedProduct = ViewedProduct(
            productId = "MLA-SPECIAL-123",
            productName = "Product with special chars: @#$%^&*()",
            thumbnailUrl = "https://example.com/image%20with%20spaces.jpg",
            timestamp = timestamp
        )
        val result = viewedProduct.toEntity()
        assertThat(result.productId).isEqualTo("MLA-SPECIAL-123")
        assertThat(result.productName).isEqualTo("Product with special chars: @#$%^&*()")
        assertThat(result.thumbnailUrl).isEqualTo("https://example.com/image%20with%20spaces.jpg")
        assertThat(result.timestamp).isEqualTo(timestamp)
    }

    @Test
    fun `round trip conversion should preserve data`() {

        val originalEntity = ViewedProductEntity(
            productId = "MLA-ROUND-TRIP",
            productName = "Round trip test product",
            thumbnailUrl = "https://example.com/round-trip.jpg",
            timestamp = System.currentTimeMillis()
        )
        val domainModel = originalEntity.toDomain()
        val convertedEntity = domainModel.toEntity()
        assertThat(convertedEntity.productId).isEqualTo(originalEntity.productId)
        assertThat(convertedEntity.productName).isEqualTo(originalEntity.productName)
        assertThat(convertedEntity.thumbnailUrl).isEqualTo(originalEntity.thumbnailUrl)
        assertThat(convertedEntity.timestamp).isEqualTo(originalEntity.timestamp)
    }

    @Test
    fun `round trip conversion with null thumbnail should preserve data`() {

        val originalEntity = ViewedProductEntity(
            productId = "MLA-NULL-THUMB",
            productName = "Product with null thumbnail",
            thumbnailUrl = null,
            timestamp = System.currentTimeMillis()
        )
        val domainModel = originalEntity.toDomain()
        val convertedEntity = domainModel.toEntity()
        assertThat(convertedEntity.productId).isEqualTo(originalEntity.productId)
        assertThat(convertedEntity.productName).isEqualTo(originalEntity.productName)
        assertThat(convertedEntity.thumbnailUrl).isEqualTo(originalEntity.thumbnailUrl)
        assertThat(convertedEntity.timestamp).isEqualTo(originalEntity.timestamp)
    }

    @Test
    fun `round trip conversion with empty fields should preserve data`() {

        val originalEntity = ViewedProductEntity(
            productId = "",
            productName = "",
            thumbnailUrl = "",
            timestamp = 0L
        )
        val domainModel = originalEntity.toDomain()
        val convertedEntity = domainModel.toEntity()
        assertThat(convertedEntity.productId).isEqualTo(originalEntity.productId)
        assertThat(convertedEntity.productName).isEqualTo(originalEntity.productName)
        assertThat(convertedEntity.thumbnailUrl).isEqualTo(originalEntity.thumbnailUrl)
        assertThat(convertedEntity.timestamp).isEqualTo(originalEntity.timestamp)
    }
}
