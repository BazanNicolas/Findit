package com.products.app.data.repository

import com.google.common.truth.Truth.assertThat
import com.products.app.core.NetworkErrorHandler
import com.products.app.data.remote.ProductsApi
import com.products.app.data.remote.dto.ProductDetailDto
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.*
import org.mockito.ArgumentMatchers.any
import java.io.IOException

@ExperimentalCoroutinesApi
class ProductDetailRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var productsApi: ProductsApi

    @Mock
    private lateinit var errorHandler: NetworkErrorHandler

    private lateinit var repository: ProductDetailRepositoryImpl

    @Before
    fun setUp() {
        repository = ProductDetailRepositoryImpl(productsApi, errorHandler)
    }

    @Test
    fun `when api returns success, should return mapped product detail`() = runTest {
        val productId = "MLA123456789"
        val apiResponse = ProductDetailDto(
            id = productId,
            catalog_product_id = productId,
            status = "active",
            pdp_types = listOf("regular"),
            domain_id = "MLA-CELLPHONES",
            permalink = "https://www.mercadolibre.com.ar/product/$productId",
            name = "iPhone 15 Pro Max",
            family_name = "iPhone 15",
            type = "product",
            buy_box_winner = null,
            pickers = null,
            pictures = null,
            description_pictures = null,
            main_features = null,
            disclaimers = null,
            attributes = null,
            short_description = null,
            parent_id = null,
            user_product = null,
            children_ids = null,
            settings = null,
            quality_type = "premium",
            release_info = null,
            presale_info = null,
            enhanced_content = null,
            tags = null,
            date_created = null,
            authorized_stores = null,
            last_updated = null,
            grouper_id = null,
            experiments = null
        )
        whenever(productsApi.getProductDetail(productId)).thenReturn(apiResponse)
        val result = repository.getProductDetail(productId)
        assertThat(result.isSuccess).isTrue()
        val productDetail = result.getOrThrow()
        assertThat(productDetail.id).isEqualTo(productId)
        assertThat(productDetail.name).isEqualTo("iPhone 15 Pro Max")
        assertThat(productDetail.status).isEqualTo("active")
        assertThat(productDetail.domainId).isEqualTo("MLA-CELLPHONES")
        assertThat(productDetail.qualityType).isEqualTo("premium")
        
        verify(productsApi).getProductDetail(productId)
    }

    @Test
    fun `when api throws IOException, should return failure result`() = runTest {

        val productId = "MLA999999999"
        val exception = RuntimeException("Network error")
        doThrow(exception).whenever(productsApi).getProductDetail(productId)
        whenever(errorHandler.handleError(exception)).thenReturn("Network error")
        val result = repository.getProductDetail(productId)
        assertThat(result.isFailure).isTrue()
        val exceptionResult = result.exceptionOrNull()
        assertThat(exceptionResult).isNotNull()
        assertThat(exceptionResult?.message).isEqualTo("Network error")
        
        verify(productsApi).getProductDetail(productId)
        verify(errorHandler).handleError(exception)
    }

    @Test
    fun `when api throws generic exception, should return failure result`() = runTest {

        val productId = "MLA888888888"
        val exception = RuntimeException("Server error")
        doThrow(exception).whenever(productsApi).getProductDetail(productId)
        whenever(errorHandler.handleError(exception)).thenReturn("Server error")
        val result = repository.getProductDetail(productId)
        assertThat(result.isFailure).isTrue()
        val exceptionResult = result.exceptionOrNull()
        assertThat(exceptionResult).isNotNull()
        assertThat(exceptionResult?.message).isEqualTo("Server error")
        
        verify(productsApi).getProductDetail(productId)
        verify(errorHandler).handleError(exception)
    }

    @Test
    fun `when api throws exception without message, should return unknown error`() = runTest {

        val productId = "MLA777777777"
        val exception = RuntimeException()
        doThrow(exception).whenever(productsApi).getProductDetail(productId)
        whenever(errorHandler.handleError(exception)).thenReturn("Unknown error")
        val result = repository.getProductDetail(productId)
        assertThat(result.isFailure).isTrue()
        val exceptionResult = result.exceptionOrNull()
        assertThat(exceptionResult).isNotNull()
        assertThat(exceptionResult?.message).isEqualTo("Unknown error")
        
        verify(productsApi).getProductDetail(productId)
        verify(errorHandler).handleError(exception)
    }

    @Test
    fun `when api returns product with null fields, should handle gracefully`() = runTest {

        val productId = "MLA666666666"
        val apiResponse = ProductDetailDto(
            id = productId,
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
        whenever(productsApi.getProductDetail(productId)).thenReturn(apiResponse)
        val result = repository.getProductDetail(productId)
        assertThat(result.isSuccess).isTrue()
        val productDetail = result.getOrThrow()
        assertThat(productDetail.id).isEqualTo(productId)
        assertThat(productDetail.catalogProductId).isEmpty()
        assertThat(productDetail.status).isEmpty()
        assertThat(productDetail.name).isEmpty()
    }

    @Test
    fun `when api returns product with empty lists, should handle correctly`() = runTest {

        val productId = "MLA555555555"
        val apiResponse = ProductDetailDto(
            id = productId,
            catalog_product_id = productId,
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
        whenever(productsApi.getProductDetail(productId)).thenReturn(apiResponse)
        val result = repository.getProductDetail(productId)
        assertThat(result.isSuccess).isTrue()
        val productDetail = result.getOrThrow()
        assertThat(productDetail.id).isEqualTo(productId)
        assertThat(productDetail.pdpTypes).isEmpty()
        assertThat(productDetail.pickers).isEmpty()
        assertThat(productDetail.pictures).isEmpty()
        assertThat(productDetail.mainFeatures).isEmpty()
        assertThat(productDetail.disclaimers).isEmpty()
        assertThat(productDetail.attributes).isEmpty()
        assertThat(productDetail.childrenIds).isEmpty()
        assertThat(productDetail.tags).isEmpty()
        assertThat(productDetail.experiments).isEmpty()
    }

    @Test
    fun `when called with empty product id, should still call api`() = runTest {

        val emptyProductId = ""
        val exception = RuntimeException("Invalid product ID")
        doThrow(exception).whenever(productsApi).getProductDetail(emptyProductId)
        whenever(errorHandler.handleError(exception)).thenReturn("Invalid product ID")
        val result = repository.getProductDetail(emptyProductId)
        assertThat(result.isFailure).isTrue()
        verify(productsApi).getProductDetail(emptyProductId)
        verify(errorHandler).handleError(exception)
    }

    @Test
    fun `when called multiple times with same product id, should call api each time`() = runTest {

        val productId = "MLA444444444"
        val apiResponse = ProductDetailDto(
            id = productId,
            catalog_product_id = productId,
            status = "active",
            pdp_types = null,
            domain_id = "MLA-CELLPHONES",
            permalink = "https://example.com",
            name = "Test Product",
            family_name = null,
            type = "product",
            buy_box_winner = null,
            pickers = null,
            pictures = null,
            description_pictures = null,
            main_features = null,
            disclaimers = null,
            attributes = null,
            short_description = null,
            parent_id = null,
            user_product = null,
            children_ids = null,
            settings = null,
            quality_type = "premium",
            release_info = null,
            presale_info = null,
            enhanced_content = null,
            tags = null,
            date_created = null,
            authorized_stores = null,
            last_updated = null,
            grouper_id = null,
            experiments = null
        )
        whenever(productsApi.getProductDetail(productId)).thenReturn(apiResponse)
        repository.getProductDetail(productId)
        repository.getProductDetail(productId)
        verify(productsApi, times(2)).getProductDetail(productId)
    }

    @Test
    fun `when called with different product ids, should call api with correct parameters`() = runTest {

        val productId1 = "MLA111111111"
        val productId2 = "MLA222222222"
        val apiResponse1 = ProductDetailDto(
            id = productId1,
            catalog_product_id = productId1,
            status = "active",
            pdp_types = null,
            domain_id = "MLA-CELLPHONES",
            permalink = "",
            name = "Product 1",
            family_name = "",
            type = "product",
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
            quality_type = "premium",
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
        val apiResponse2 = ProductDetailDto(
            id = productId2,
            catalog_product_id = productId2,
            status = "active",
            pdp_types = null,
            domain_id = "MLA-CELLPHONES",
            permalink = "",
            name = "Product 2",
            family_name = "",
            type = "product",
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
            quality_type = "premium",
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
        whenever(productsApi.getProductDetail(productId1)).thenReturn(apiResponse1)
        whenever(productsApi.getProductDetail(productId2)).thenReturn(apiResponse2)
        repository.getProductDetail(productId1)
        repository.getProductDetail(productId2)
        verify(productsApi).getProductDetail(productId1)
        verify(productsApi).getProductDetail(productId2)
    }

    @Test
    fun `repository should be properly initialized`() = runTest {

        assertThat(repository).isNotNull()
    }

    @Test
    fun `when api returns product with complex nested data, should map correctly`() = runTest {

        val productId = "MLA333333333"
        val apiResponse = ProductDetailDto(
            id = productId,
            catalog_product_id = productId,
            status = "active",
            pdp_types = listOf("regular", "premium"),
            domain_id = "MLA-CELLPHONES",
            permalink = "https://www.mercadolibre.com.ar/product/$productId",
            name = "Complex Product",
            family_name = "Complex Family",
            type = "product",
            buy_box_winner = null,
            pickers = null,
            pictures = null,
            description_pictures = null,
            main_features = null,
            disclaimers = null,
            attributes = null,
            short_description = null,
            parent_id = null,
            user_product = null,
            children_ids = listOf("child1", "child2"),
            settings = null,
            quality_type = "premium",
            release_info = null,
            presale_info = null,
            enhanced_content = null,
            tags = listOf("tag1", "tag2"),
            date_created = "2024-01-01T00:00:00.000Z",
            authorized_stores = null,
            last_updated = "2024-01-01T00:00:00.000Z",
            grouper_id = "grouper123",
            experiments = mapOf("exp1" to "value1", "exp2" to "value2")
        )
        whenever(productsApi.getProductDetail(productId)).thenReturn(apiResponse)
        val result = repository.getProductDetail(productId)
        assertThat(result.isSuccess).isTrue()
        val productDetail = result.getOrThrow()
        assertThat(productDetail.id).isEqualTo(productId)
        assertThat(productDetail.pdpTypes).isEqualTo(listOf("regular", "premium"))
        assertThat(productDetail.childrenIds).isEqualTo(listOf("child1", "child2"))
        assertThat(productDetail.tags).isEqualTo(listOf("tag1", "tag2"))
        assertThat(productDetail.dateCreated).isEqualTo("2024-01-01T00:00:00.000Z")
        assertThat(productDetail.lastUpdated).isEqualTo("2024-01-01T00:00:00.000Z")
        assertThat(productDetail.grouperId).isEqualTo("grouper123")
        assertThat(productDetail.experiments).isEqualTo(mapOf("exp1" to "value1", "exp2" to "value2"))
    }
}
