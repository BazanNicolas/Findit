package com.products.app.domain.usecase

import com.products.app.domain.repository.SearchHistoryRepository
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class SaveSearchUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: SearchHistoryRepository

    private lateinit var useCase: SaveSearchUseCase

    @Before
    fun setUp() {
        useCase = SaveSearchUseCase(repository)
    }

    @Test
    fun `when query is valid, should save search to repository`() = runTest {
        // Given
        val query = "iphone 15"

        // When
        useCase(query)

        // Then
        verify(repository).saveSearch(query)
    }

    @Test
    fun `when query has leading and trailing spaces, should save trimmed query`() = runTest {
        // Given
        val queryWithSpaces = "  samsung galaxy  "
        val expectedTrimmedQuery = "samsung galaxy"

        // When
        useCase(queryWithSpaces)

        // Then
        verify(repository).saveSearch(expectedTrimmedQuery)
    }

    @Test
    fun `when query is empty string, should not save search`() = runTest {
        // Given
        val emptyQuery = ""

        // When
        useCase(emptyQuery)

        // Then
        verify(repository, never()).saveSearch(emptyQuery)
    }

    @Test
    fun `when query is only spaces, should not save search`() = runTest {
        // Given
        val spacesQuery = "   "

        // When
        useCase(spacesQuery)

        // Then
        verify(repository, never()).saveSearch(org.mockito.kotlin.any())
    }

    @Test
    fun `when query is single character, should save search`() = runTest {
        // Given
        val singleCharQuery = "a"

        // When
        useCase(singleCharQuery)

        // Then
        verify(repository).saveSearch(singleCharQuery)
    }

    @Test
    fun `when query is very long, should save complete query`() = runTest {
        // Given
        val longQuery = "this is a very long search query with many words that represents a real user search"

        // When
        useCase(longQuery)

        // Then
        verify(repository).saveSearch(longQuery)
    }

    @Test
    fun `when query contains special characters, should save query as is`() = runTest {
        // Given
        val specialCharQuery = "iphone @#$% 15-pro"

        // When
        useCase(specialCharQuery)

        // Then
        verify(repository).saveSearch(specialCharQuery)
    }

    @Test
    fun `when query contains numbers, should save query correctly`() = runTest {
        // Given
        val numericQuery = "samsung s24 ultra 512gb"

        // When
        useCase(numericQuery)

        // Then
        verify(repository).saveSearch(numericQuery)
    }

    @Test
    fun `when multiple saves are called, should call repository each time`() = runTest {
        // Given
        val query1 = "first search"
        val query2 = "second search"

        // When
        useCase(query1)
        useCase(query2)

        // Then
        verify(repository).saveSearch(query1)
        verify(repository).saveSearch(query2)
    }
}
