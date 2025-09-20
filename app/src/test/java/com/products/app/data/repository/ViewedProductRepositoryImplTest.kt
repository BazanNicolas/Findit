package com.products.app.data.repository

import com.google.common.truth.Truth.assertThat
import com.products.app.data.local.dao.ViewedProductDao
import com.products.app.data.local.entity.ViewedProductEntity
import com.products.app.domain.model.ViewedProduct
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class ViewedProductRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var viewedProductDao: ViewedProductDao

    private lateinit var repository: ViewedProductRepositoryImpl

    @Before
    fun setUp() {
        repository = ViewedProductRepositoryImpl(viewedProductDao)
    }

    @Test
    fun `getRecentViewedProducts should return mapped domain models`() = runTest {

        val limit = 5
        val entities = listOf(
            ViewedProductEntity(
                productId = "MLA111",
                productName = "Product 1",
                thumbnailUrl = "thumb1.jpg",
                timestamp = System.currentTimeMillis()
            ),
            ViewedProductEntity(
                productId = "MLA222",
                productName = "Product 2",
                thumbnailUrl = "thumb2.jpg",
                timestamp = System.currentTimeMillis() - 1000
            )
        )
        whenever(viewedProductDao.getRecentViewedProducts(limit)).thenReturn(flowOf(entities))
        val result = repository.getRecentViewedProducts(limit).first()
        assertThat(result).hasSize(2)
        assertThat(result[0].productId).isEqualTo("MLA111")
        assertThat(result[0].productName).isEqualTo("Product 1")
        assertThat(result[0].thumbnailUrl).isEqualTo("thumb1.jpg")
        assertThat(result[1].productId).isEqualTo("MLA222")
        assertThat(result[1].productName).isEqualTo("Product 2")
        assertThat(result[1].thumbnailUrl).isEqualTo("thumb2.jpg")
        
        verify(viewedProductDao).getRecentViewedProducts(limit)
    }

    @Test
    fun `getRecentViewedProducts with empty list should return empty list`() = runTest {

        val limit = 10
        whenever(viewedProductDao.getRecentViewedProducts(limit)).thenReturn(flowOf(emptyList()))
        val result = repository.getRecentViewedProducts(limit).first()
        assertThat(result).isEmpty()
        verify(viewedProductDao).getRecentViewedProducts(limit)
    }

    @Test
    fun `getRecentViewedProducts with different limits should call dao with correct limit`() = runTest {

        val limit1 = 5
        val limit2 = 20
        whenever(viewedProductDao.getRecentViewedProducts(any())).thenReturn(flowOf(emptyList()))
        repository.getRecentViewedProducts(limit1).first()
        repository.getRecentViewedProducts(limit2).first()
        verify(viewedProductDao).getRecentViewedProducts(limit1)
        verify(viewedProductDao).getRecentViewedProducts(limit2)
    }

    @Test
    fun `saveViewedProduct should call dao with mapped entity`() = runTest {

        val viewedProduct = MockDataFactory.createViewedProduct(
            productId = "MLA123456789",
            productName = "iPhone 15 Pro Max"
        )
        repository.saveViewedProduct(viewedProduct)
        val entityCaptor = argumentCaptor<ViewedProductEntity>()
        verify(viewedProductDao).insertViewedProduct(entityCaptor.capture())
        
        val capturedEntity = entityCaptor.firstValue
        assertThat(capturedEntity.productId).isEqualTo("MLA123456789")
        assertThat(capturedEntity.productName).isEqualTo("iPhone 15 Pro Max")
        assertThat(capturedEntity.thumbnailUrl).isEqualTo(viewedProduct.thumbnailUrl)
        assertThat(capturedEntity.timestamp).isEqualTo(viewedProduct.timestamp)
    }

    @Test
    fun `saveViewedProduct with null thumbnail should handle correctly`() = runTest {

        val viewedProduct = ViewedProduct(
            productId = "MLA999999999",
            productName = "Product without thumbnail",
            thumbnailUrl = null,
            timestamp = System.currentTimeMillis()
        )
        repository.saveViewedProduct(viewedProduct)
        val entityCaptor = argumentCaptor<ViewedProductEntity>()
        verify(viewedProductDao).insertViewedProduct(entityCaptor.capture())
        
        val capturedEntity = entityCaptor.firstValue
        assertThat(capturedEntity.productId).isEqualTo("MLA999999999")
        assertThat(capturedEntity.productName).isEqualTo("Product without thumbnail")
        assertThat(capturedEntity.thumbnailUrl).isNull()
    }

    @Test
    fun `deleteViewedProduct should call dao with mapped entity`() = runTest {

        val viewedProduct = MockDataFactory.createViewedProduct(
            productId = "MLA888888888",
            productName = "Product to delete"
        )
        repository.deleteViewedProduct(viewedProduct)
        val entityCaptor = argumentCaptor<ViewedProductEntity>()
        verify(viewedProductDao).deleteViewedProduct(entityCaptor.capture())
        
        val capturedEntity = entityCaptor.firstValue
        assertThat(capturedEntity.productId).isEqualTo("MLA888888888")
        assertThat(capturedEntity.productName).isEqualTo("Product to delete")
    }

    @Test
    fun `clearAllViewedProducts should call dao clear method`() = runTest {

        repository.clearAllViewedProducts()
        verify(viewedProductDao).clearAllViewedProducts()
    }

    @Test
    fun `clearOldViewedProducts should call dao with correct cutoff time`() = runTest {

        val currentTime = System.currentTimeMillis()
        val expectedCutoffTime = currentTime - (30 * 24 * 60 * 60 * 1000L) // 30 days
        repository.clearOldViewedProducts()
        val cutoffTimeCaptor = argumentCaptor<Long>()
        verify(viewedProductDao).deleteOldViewedProducts(cutoffTimeCaptor.capture())
        
        val capturedCutoffTime = cutoffTimeCaptor.firstValue
        // Allow for small time differences due to test execution time
        assertThat(capturedCutoffTime).isAtMost(expectedCutoffTime + 1000)
        assertThat(capturedCutoffTime).isAtLeast(expectedCutoffTime - 1000)
    }

    @Test
    fun `multiple saveViewedProduct calls should call dao multiple times`() = runTest {

        val viewedProduct1 = MockDataFactory.createViewedProduct(productId = "MLA111")
        val viewedProduct2 = MockDataFactory.createViewedProduct(productId = "MLA222")
        repository.saveViewedProduct(viewedProduct1)
        repository.saveViewedProduct(viewedProduct2)
        verify(viewedProductDao, times(2)).insertViewedProduct(any())
    }

    @Test
    fun `multiple deleteViewedProduct calls should call dao multiple times`() = runTest {

        val viewedProduct1 = MockDataFactory.createViewedProduct(productId = "MLA111")
        val viewedProduct2 = MockDataFactory.createViewedProduct(productId = "MLA222")
        repository.deleteViewedProduct(viewedProduct1)
        repository.deleteViewedProduct(viewedProduct2)
        verify(viewedProductDao, times(2)).deleteViewedProduct(any())
    }

    @Test
    fun `repository should be properly initialized`() = runTest {

        assertThat(repository).isNotNull()
    }

    @Test
    fun `getRecentViewedProducts should handle dao flow updates`() = runTest {

        val limit = 3
        val initialEntities = listOf(
            ViewedProductEntity(
                productId = "MLA111",
                productName = "Product 1",
                thumbnailUrl = "thumb1.jpg",
                timestamp = System.currentTimeMillis()
            )
        )
        val updatedEntities = listOf(
            ViewedProductEntity(
                productId = "MLA111",
                productName = "Product 1",
                thumbnailUrl = "thumb1.jpg",
                timestamp = System.currentTimeMillis()
            ),
            ViewedProductEntity(
                productId = "MLA222",
                productName = "Product 2",
                thumbnailUrl = "thumb2.jpg",
                timestamp = System.currentTimeMillis() - 1000
            )
        )
        whenever(viewedProductDao.getRecentViewedProducts(limit))
            .thenReturn(flowOf(initialEntities, updatedEntities))
        val result = repository.getRecentViewedProducts(limit).first()
        assertThat(result).hasSize(1) // First emission
        assertThat(result[0].productId).isEqualTo("MLA111")
        
        verify(viewedProductDao).getRecentViewedProducts(limit)
    }

    @Test
    fun `saveViewedProduct with empty product name should handle correctly`() = runTest {

        val viewedProduct = ViewedProduct(
            productId = "MLA777777777",
            productName = "",
            thumbnailUrl = "thumb.jpg",
            timestamp = System.currentTimeMillis()
        )
        repository.saveViewedProduct(viewedProduct)
        val entityCaptor = argumentCaptor<ViewedProductEntity>()
        verify(viewedProductDao).insertViewedProduct(entityCaptor.capture())
        
        val capturedEntity = entityCaptor.firstValue
        assertThat(capturedEntity.productId).isEqualTo("MLA777777777")
        assertThat(capturedEntity.productName).isEmpty()
    }

    @Test
    fun `getRecentViewedProducts with default limit should use default value`() = runTest {

        val defaultLimit = 10
        whenever(viewedProductDao.getRecentViewedProducts(defaultLimit)).thenReturn(flowOf(emptyList()))
        repository.getRecentViewedProducts().first()
        verify(viewedProductDao).getRecentViewedProducts(defaultLimit)
    }

    @Test
    fun `saveViewedProduct with very long product name should handle correctly`() = runTest {

        val longProductName = "This is a very long product name that might exceed normal limits and should be handled correctly by the repository"
        val viewedProduct = ViewedProduct(
            productId = "MLA666666666",
            productName = longProductName,
            thumbnailUrl = "thumb.jpg",
            timestamp = System.currentTimeMillis()
        )
        repository.saveViewedProduct(viewedProduct)
        val entityCaptor = argumentCaptor<ViewedProductEntity>()
        verify(viewedProductDao).insertViewedProduct(entityCaptor.capture())
        
        val capturedEntity = entityCaptor.firstValue
        assertThat(capturedEntity.productName).isEqualTo(longProductName)
    }
}
