package com.products.app.domain.usecase

import com.products.app.domain.repository.ViewedProductRepository
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class SaveViewedProductUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: ViewedProductRepository

    private lateinit var useCase: SaveViewedProductUseCase

    @Before
    fun setUp() {
        useCase = SaveViewedProductUseCase(repository)
    }

    @Test
    fun `when product is valid, should save viewed product to repository`() = runTest {
        // Given
        val product = MockDataFactory.createProduct()

        // When
        useCase(product)

        // Then
        verify(repository).saveViewedProduct(any())
    }

    @Test
    fun `when product has complete information, should save all data`() = runTest {
        // Given
        val completeProduct = MockDataFactory.createProduct(
            id = "MLA123456789",
            name = "iPhone 15 Pro Max 256GB",
            thumbnailUrl = "https://example.com/image.jpg"
        )

        // When
        useCase(completeProduct)

        // Then
        verify(repository).saveViewedProduct(any())
    }

    @Test
    fun `when product has no thumbnail, should still save product`() = runTest {
        // Given
        val productWithoutThumbnail = MockDataFactory.createProduct(
            thumbnailUrl = null
        )

        // When
        useCase(productWithoutThumbnail)

        // Then
        verify(repository).saveViewedProduct(any())
    }

    @Test
    fun `when multiple products are saved, should call repository for each`() = runTest {
        // Given
        val product1 = MockDataFactory.createProduct(id = "MLA111111111")
        val product2 = MockDataFactory.createProduct(id = "MLA222222222")

        // When
        useCase(product1)
        useCase(product2)

        // Then
        verify(repository, times(2)).saveViewedProduct(any())
    }

    @Test
    fun `when same product is saved multiple times, should call repository each time`() = runTest {
        // Given
        val product = MockDataFactory.createProduct()

        // When
        useCase(product)
        useCase(product)

        // Then
        verify(repository, times(2)).saveViewedProduct(any())
    }
}
