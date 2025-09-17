package com.products.app.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.products.app.core.AppResult
import com.products.app.core.PaginationConstants
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
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class LoadMoreProductsUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: ProductsRepository

    private lateinit var useCase: LoadMoreProductsUseCase

    @Before
    fun setUp() {
        useCase = LoadMoreProductsUseCase(repository)
    }

    @Test
    fun `when loading more products, should calculate correct next offset`() = runTest {
        // Given
        val query = "iphone"
        val currentOffset = 20
        val limit = 10
        val expectedNextOffset = currentOffset + limit
        val expectedResult = MockDataFactory.createProductSearchResult(
            offset = expectedNextOffset,
            limit = limit
        )
        whenever(repository.search(query, expectedNextOffset, limit))
            .thenReturn(AppResult.Success(expectedResult))

        // When
        val result = useCase(query, currentOffset, limit)

        // Then
        val actualResult = result.assertSuccess()
        assertThat(actualResult.paging.offset).isEqualTo(expectedNextOffset)
        verify(repository).search(query, expectedNextOffset, limit)
    }

    @Test
    fun `when using default limit, should use pagination constant`() = runTest {
        // Given
        val query = "samsung"
        val currentOffset = 0
        val expectedNextOffset = PaginationConstants.DEFAULT_PAGE_SIZE
        val expectedResult = MockDataFactory.createProductSearchResult(
            offset = expectedNextOffset,
            limit = PaginationConstants.DEFAULT_PAGE_SIZE
        )
        whenever(repository.search(query, expectedNextOffset, PaginationConstants.DEFAULT_PAGE_SIZE))
            .thenReturn(AppResult.Success(expectedResult))

        // When
        val result = useCase(query, currentOffset, PaginationConstants.DEFAULT_PAGE_SIZE) // Using default limit

        // Then
        result.assertSuccess()
        verify(repository).search(query, expectedNextOffset, PaginationConstants.DEFAULT_PAGE_SIZE)
    }

    @Test
    fun `when repository returns error, should return error result`() = runTest {
        // Given
        val query = "test"
        val currentOffset = 10
        val limit = 5
        val errorMessage = "Network timeout"
        whenever(repository.search(query, 15, limit)).thenReturn(AppResult.Error(errorMessage))

        // When
        val result = useCase(query, currentOffset, limit)

        // Then
        result.assertError(errorMessage)
    }

    @Test
    fun `when loading first page with offset zero, should start from page size`() = runTest {
        // Given
        val query = "laptop"
        val currentOffset = 0
        val limit = 20
        val expectedNextOffset = 20
        val expectedResult = MockDataFactory.createProductSearchResult(
            offset = expectedNextOffset,
            limit = limit
        )
        whenever(repository.search(query, expectedNextOffset, limit))
            .thenReturn(AppResult.Success(expectedResult))

        // When
        val result = useCase(query, currentOffset, limit)

        // Then
        val actualResult = result.assertSuccess()
        assertThat(actualResult.paging.offset).isEqualTo(expectedNextOffset)
    }

    @Test
    fun `when query has spaces, should trim query before calling repository`() = runTest {
        // Given
        val queryWithSpaces = "  tablet  "
        val trimmedQuery = "tablet"
        val currentOffset = 30
        val limit = 15
        val expectedNextOffset = 45
        val expectedResult = MockDataFactory.createProductSearchResult()
        whenever(repository.search(trimmedQuery, expectedNextOffset, limit))
            .thenReturn(AppResult.Success(expectedResult))

        // When
        val result = useCase(queryWithSpaces, currentOffset, limit)

        // Then
        result.assertSuccess()
        verify(repository).search(trimmedQuery, expectedNextOffset, limit)
    }

    @Test
    fun `when loading with large offset, should handle calculation correctly`() = runTest {
        // Given
        val query = "phone"
        val currentOffset = 980
        val limit = 20
        val expectedNextOffset = 1000
        val expectedResult = MockDataFactory.createProductSearchResult(
            products = emptyList(), // End of results
            offset = expectedNextOffset,
            limit = limit
        )
        whenever(repository.search(query, expectedNextOffset, limit))
            .thenReturn(AppResult.Success(expectedResult))

        // When
        val result = useCase(query, currentOffset, limit)

        // Then
        val actualResult = result.assertSuccess()
        assertThat(actualResult.products).isEmpty()
        assertThat(actualResult.paging.offset).isEqualTo(expectedNextOffset)
    }
}
