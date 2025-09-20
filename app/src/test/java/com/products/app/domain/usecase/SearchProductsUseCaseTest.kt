package com.products.app.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.products.app.core.AppResult
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
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
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Test suite for SearchProductsUseCase.
 * 
 * This test class verifies the business logic of product searching,
 * including query validation, parameter handling, and error scenarios.
 * It uses Mockito for mocking dependencies and Truth for assertions.
 * 
 * Test scenarios covered:
 * - Successful product search with valid query
 * - Query trimming (removing leading/trailing spaces)
 * - Error handling when repository fails
 * - Custom pagination parameters
 * - Edge cases (empty queries, whitespace-only queries)
 * - Default parameter usage
 */
@ExperimentalCoroutinesApi
class SearchProductsUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: ProductsRepository

    private lateinit var useCase: SearchProductsUseCase

    @Before
    fun setUp() {
        useCase = SearchProductsUseCase(repository)
    }

    /**
     * Tests successful product search with a valid query.
     * 
     * Verifies that the use case correctly delegates to the repository
     * and returns the expected search results.
     */
    @Test
    fun `when query is valid, should return success result`() = runTest {
        // Given
        val query = "iphone"
        val expectedResult = MockDataFactory.createProductSearchResult()
        whenever(repository.search(query, 0, 10)).thenReturn(AppResult.Success(expectedResult))
        
        // When
        val result = useCase(query, 0, 10)
        
        // Then
        val actualResult = result.assertSuccess()
        assertThat(actualResult).isEqualTo(expectedResult)
        verify(repository).search(query, 0, 10)
    }

    /**
     * Tests that queries with leading and trailing spaces are properly trimmed.
     * 
     * Verifies that the use case automatically trims whitespace from the input
     * query before passing it to the repository.
     */
    @Test
    fun `when query has leading and trailing spaces, should trim query`() = runTest {
        // Given
        val queryWithSpaces = "  iphone  "
        val trimmedQuery = "iphone"
        val expectedResult = MockDataFactory.createProductSearchResult()
        whenever(repository.search(trimmedQuery, 0, 10)).thenReturn(AppResult.Success(expectedResult))
        
        // When
        val result = useCase(queryWithSpaces, 0, 10)
        
        // Then
        result.assertSuccess()
        verify(repository).search(trimmedQuery, 0, 10)
    }

    /**
     * Tests error handling when the repository returns an error.
     * 
     * Verifies that errors from the repository are properly propagated
     * to the use case caller without modification.
     */
    @Test
    fun `when repository returns error, should return error result`() = runTest {
        // Given
        val query = "iphone"
        val errorMessage = "Network error"
        whenever(repository.search(any(), any(), any())).thenReturn(AppResult.Error(errorMessage))
        
        // When
        val result = useCase(query, 0, 10)
        
        // Then
        result.assertError(errorMessage)
        verify(repository).search(query, 0, 10)
    }

    @Test
    fun `when called with custom offset and limit, should pass parameters correctly`() = runTest {

        val query = "samsung"
        val offset = 20
        val limit = 5
        val expectedResult = MockDataFactory.createProductSearchResult(
            products = MockDataFactory.createProductList(5),
            offset = offset,
            limit = limit
        )
        whenever(repository.search(query, offset, limit)).thenReturn(AppResult.Success(expectedResult))
        val result = useCase(query, offset, limit)
        val actualResult = result.assertSuccess()
        assertThat(actualResult.paging.offset).isEqualTo(offset)
        assertThat(actualResult.paging.limit).isEqualTo(limit)
        verify(repository).search(query, offset, limit)
    }

    @Test
    fun `when query is empty string, should still call repository with empty query`() = runTest {

        val emptyQuery = ""
        val expectedResult = MockDataFactory.createProductSearchResult(products = emptyList())
        whenever(repository.search(emptyQuery, 0, 10)).thenReturn(AppResult.Success(expectedResult))
        val result = useCase(emptyQuery, 0, 10)
        val actualResult = result.assertSuccess()
        assertThat(actualResult.products).isEmpty()
        verify(repository).search(emptyQuery, 0, 10)
    }

    @Test
    fun `when query is only spaces, should call repository with empty string`() = runTest {

        val spacesQuery = "   "
        val expectedResult = MockDataFactory.createProductSearchResult(products = emptyList())
        whenever(repository.search("", 0, 10)).thenReturn(AppResult.Success(expectedResult))
        val result = useCase(spacesQuery, 0, 10)
        result.assertSuccess()
        verify(repository).search("", 0, 10)
    }

    @Test
    fun `when using default parameters, should use correct defaults`() = runTest {

        val query = "test"
        val expectedResult = MockDataFactory.createProductSearchResult()
        whenever(repository.search(query, 0, 10)).thenReturn(AppResult.Success(expectedResult))
        val result = useCase(query) // Using defaults
        result.assertSuccess()
        verify(repository).search(query, 0, 10)
    }
}
