package com.products.app.domain.usecase

import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.repository.ViewedProductRepository
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.ArgumentMatchers.any

@ExperimentalCoroutinesApi
class GetRecentViewedProductsUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var viewedProductRepository: ViewedProductRepository

    private lateinit var useCase: GetRecentViewedProductsUseCase

    @Before
    fun setUp() {
        useCase = GetRecentViewedProductsUseCase(viewedProductRepository)
    }

    @Test
    fun `invoke should call repository getRecentViewedProducts with correct limit`() = runTest {

        val limit = 10
        val recentProducts = MockDataFactory.createViewedProductList(5)
        whenever(viewedProductRepository.getRecentViewedProducts(limit)).thenReturn(flowOf(recentProducts))
        val result = useCase(limit).first()
        assert(result == recentProducts)
        verify(viewedProductRepository).getRecentViewedProducts(limit)
    }

    @Test
    fun `invoke with default limit should use default value`() = runTest {

        val defaultLimit = 10
        val recentProducts = MockDataFactory.createViewedProductList(3)
        whenever(viewedProductRepository.getRecentViewedProducts(defaultLimit)).thenReturn(flowOf(recentProducts))
        val result = useCase().first()
        assert(result == recentProducts)
        verify(viewedProductRepository).getRecentViewedProducts(defaultLimit)
    }

    @Test
    fun `invoke with zero limit should call repository`() = runTest {

        val zeroLimit = 0
        val recentProducts = emptyList<ViewedProduct>()
        whenever(viewedProductRepository.getRecentViewedProducts(zeroLimit)).thenReturn(flowOf(recentProducts))
        val result = useCase(zeroLimit).first()
        assert(result.isEmpty())
        verify(viewedProductRepository).getRecentViewedProducts(zeroLimit)
    }

    @Test
    fun `invoke with negative limit should call repository`() = runTest {

        val negativeLimit = -5
        val recentProducts = emptyList<ViewedProduct>()
        whenever(viewedProductRepository.getRecentViewedProducts(negativeLimit)).thenReturn(flowOf(recentProducts))
        val result = useCase(negativeLimit).first()
        assert(result.isEmpty())
        verify(viewedProductRepository).getRecentViewedProducts(negativeLimit)
    }

    @Test
    fun `invoke with large limit should call repository`() = runTest {

        val largeLimit = 1000
        val recentProducts = MockDataFactory.createViewedProductList(10)
        whenever(viewedProductRepository.getRecentViewedProducts(largeLimit)).thenReturn(flowOf(recentProducts))
        val result = useCase(largeLimit).first()
        assert(result == recentProducts)
        verify(viewedProductRepository).getRecentViewedProducts(largeLimit)
    }

    @Test
    fun `invoke with empty result should return empty list`() = runTest {

        val limit = 10
        val emptyProducts = emptyList<ViewedProduct>()
        whenever(viewedProductRepository.getRecentViewedProducts(limit)).thenReturn(flowOf(emptyProducts))
        val result = useCase(limit).first()
        assert(result.isEmpty())
        verify(viewedProductRepository).getRecentViewedProducts(limit)
    }

    @Test
    fun `multiple invocations with different limits should call repository multiple times`() = runTest {

        val limit1 = 5
        val limit2 = 20
        val recentProducts = MockDataFactory.createViewedProductList(3)
        whenever(viewedProductRepository.getRecentViewedProducts(limit1)).thenReturn(flowOf(recentProducts))
        whenever(viewedProductRepository.getRecentViewedProducts(limit2)).thenReturn(flowOf(recentProducts))
        useCase(limit1).first()
        useCase(limit2).first()
        verify(viewedProductRepository).getRecentViewedProducts(limit1)
        verify(viewedProductRepository).getRecentViewedProducts(limit2)
    }
    @Test
    fun `invoke should return flow that emits recent products`() = runTest {

        val limit = 10
        val initialProducts = MockDataFactory.createViewedProductList(2)
        val updatedProducts = MockDataFactory.createViewedProductList(4)
        whenever(viewedProductRepository.getRecentViewedProducts(limit))
            .thenReturn(flowOf(initialProducts, updatedProducts))
        val result = useCase(limit).first()
        assert(result == initialProducts)
        verify(viewedProductRepository).getRecentViewedProducts(limit)
    }

    @Test
    fun `invoke with products containing null thumbnails should handle correctly`() = runTest {

        val limit = 5
        val productsWithNullThumbnails = listOf(
            ViewedProduct(
                productId = "MLA111",
                productName = "Product 1",
                thumbnailUrl = null,
                timestamp = System.currentTimeMillis()
            ),
            MockDataFactory.createViewedProduct(
                productId = "MLA222",
                productName = "Product 2",
                thumbnailUrl = "thumb2.jpg"
            )
        )
        whenever(viewedProductRepository.getRecentViewedProducts(limit)).thenReturn(flowOf(productsWithNullThumbnails))
        val result = useCase(limit).first()
        assert(result == productsWithNullThumbnails)
        assert(result[0].thumbnailUrl == null)
        assert(result[1].thumbnailUrl == "thumb2.jpg")
        verify(viewedProductRepository).getRecentViewedProducts(limit)
    }

    @Test
    fun `invoke with products containing empty names should handle correctly`() = runTest {

        val limit = 5
        val productsWithEmptyNames = listOf(
            MockDataFactory.createViewedProduct(
                productId = "MLA111",
                productName = ""
            ),
            MockDataFactory.createViewedProduct(
                productId = "MLA222",
                productName = "Valid Product"
            )
        )
        whenever(viewedProductRepository.getRecentViewedProducts(limit)).thenReturn(flowOf(productsWithEmptyNames))
        val result = useCase(limit).first()
        assert(result == productsWithEmptyNames)
        assert(result[0].productName.isEmpty())
        assert(result[1].productName == "Valid Product")
        verify(viewedProductRepository).getRecentViewedProducts(limit)
    }
}
