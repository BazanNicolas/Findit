package com.products.app.data.repository

import com.google.common.truth.Truth.assertThat
import com.products.app.data.remote.ProductsApi
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import com.products.app.util.assertError
import com.products.app.util.assertSuccess
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any
import java.io.IOException

@ExperimentalCoroutinesApi
class ProductsRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var api: ProductsApi

    private lateinit var repository: ProductsRepositoryImpl

    @Before
    fun setUp() {
        repository = ProductsRepositoryImpl(api)
    }

    @Test
    fun `when api returns success, should return mapped domain result`() = runTest {
        // Given
        val query = "iphone"
        val offset = 0
        val limit = 10
        val apiResponse = MockDataFactory.createSearchResponseDto()
        whenever(api.searchProducts(query, "MLA", "active", offset, limit)).thenReturn(apiResponse)

        // When
        val result = repository.search(query, offset, limit)

        // Then
        val actualResult = result.assertSuccess()
        assertThat(actualResult.products).hasSize(1)
        assertThat(actualResult.paging.total).isEqualTo(1000)
        assertThat(actualResult.paging.offset).isEqualTo(0)
        assertThat(actualResult.paging.limit).isEqualTo(10)
        verify(api).searchProducts(query, "MLA", "active", offset, limit)
    }

    @Test
    fun `repository should be properly initialized`() = runTest {
        // When & Then - Verify repository exists and is properly constructed
        assertThat(repository).isNotNull()
    }

    @Test
    fun `when api throws generic exception, should return error result with message`() = runTest {
        // Given
        val query = "laptop"
        val offset = 20
        val limit = 5
        val exception = RuntimeException("Server error")
        whenever(api.searchProducts(query, "MLA", "active", offset, limit)).thenThrow(exception)

        // When
        val result = repository.search(query, offset, limit)

        // Then
        result.assertError("Server error")
    }

    @Test
    fun `when api throws exception without message, should return unknown error`() = runTest {
        // Given
        val query = "tablet"
        val exception = RuntimeException()
        whenever(api.searchProducts(query, "MLA", "active", 0, 10)).thenThrow(exception)

        // When
        val result = repository.search(query, 0, 10)

        // Then
        result.assertError("Unknown error")
    }

    @Test
    fun `when api returns empty results, should return empty products list`() = runTest {
        // Given
        val query = "nonexistentproduct"
        val emptyResponse = MockDataFactory.createSearchResponseDto(
            results = emptyList(),
            paging = MockDataFactory.createPagingDto(total = 0)
        )
        whenever(api.searchProducts(query, "MLA", "active", 0, 10)).thenReturn(emptyResponse)

        // When
        val result = repository.search(query, 0, 10)

        // Then
        val actualResult = result.assertSuccess()
        assertThat(actualResult.products).isEmpty()
        assertThat(actualResult.paging.total).isEqualTo(0)
    }

    @Test
    fun `when api is called with different parameters, should pass them correctly`() = runTest {
        // Given
        val query = "phone"
        val offset = 50
        val limit = 25
        val customResponse = MockDataFactory.createSearchResponseDto(
            paging = MockDataFactory.createPagingDto(
                total = 500,
                offset = offset,
                limit = limit
            )
        )
        whenever(api.searchProducts(query, "MLA", "active", offset, limit)).thenReturn(customResponse)

        // When
        val result = repository.search(query, offset, limit)

        // Then
        val actualResult = result.assertSuccess()
        assertThat(actualResult.paging.offset).isEqualTo(offset)
        assertThat(actualResult.paging.limit).isEqualTo(limit)
        verify(api).searchProducts(query, "MLA", "active", offset, limit)
    }

    @Test
    fun `when api returns multiple products, should map all products correctly`() = runTest {
        // Given
        val query = "smartphone"
        val multipleProducts = listOf(
            MockDataFactory.createProductDto(id = "MLA111", name = "Product 1"),
            MockDataFactory.createProductDto(id = "MLA222", name = "Product 2"),
            MockDataFactory.createProductDto(id = "MLA333", name = "Product 3")
        )
        val response = MockDataFactory.createSearchResponseDto(
            results = multipleProducts,
            paging = MockDataFactory.createPagingDto(total = 3)
        )
        whenever(api.searchProducts(query, "MLA", "active", 0, 10)).thenReturn(response)

        // When
        val result = repository.search(query, 0, 10)

        // Then
        val actualResult = result.assertSuccess()
        assertThat(actualResult.products).hasSize(3)
        assertThat(actualResult.products[0].id).isEqualTo("MLA111")
        assertThat(actualResult.products[1].id).isEqualTo("MLA222")
        assertThat(actualResult.products[2].id).isEqualTo("MLA333")
    }

    @Test
    fun `when query is empty, should still call api and return result`() = runTest {
        // Given
        val emptyQuery = ""
        val response = MockDataFactory.createSearchResponseDto()
        whenever(api.searchProducts(emptyQuery, "MLA", "active", 0, 10)).thenReturn(response)

        // When
        val result = repository.search(emptyQuery, 0, 10)

        // Then
        result.assertSuccess()
        verify(api).searchProducts(emptyQuery, "MLA", "active", 0, 10)
    }
}
