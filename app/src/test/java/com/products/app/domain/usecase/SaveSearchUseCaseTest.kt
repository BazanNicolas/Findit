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

        val query = "iphone 15"
        useCase(query)
        verify(repository).saveSearch(query)
    }

    @Test
    fun `when query has leading and trailing spaces, should save trimmed query`() = runTest {

        val queryWithSpaces = "  samsung galaxy  "
        val expectedTrimmedQuery = "samsung galaxy"
        useCase(queryWithSpaces)
        verify(repository).saveSearch(expectedTrimmedQuery)
    }

    @Test
    fun `when query is empty string, should not save search`() = runTest {

        val emptyQuery = ""
        useCase(emptyQuery)
        verify(repository, never()).saveSearch(emptyQuery)
    }

    @Test
    fun `when query is only spaces, should not save search`() = runTest {

        val spacesQuery = "   "
        useCase(spacesQuery)
        verify(repository, never()).saveSearch(org.mockito.kotlin.any())
    }

    @Test
    fun `when query is single character, should save search`() = runTest {

        val singleCharQuery = "a"
        useCase(singleCharQuery)
        verify(repository).saveSearch(singleCharQuery)
    }

    @Test
    fun `when query is very long, should save complete query`() = runTest {

        val longQuery = "this is a very long search query with many words that represents a real user search"
        useCase(longQuery)
        verify(repository).saveSearch(longQuery)
    }

    @Test
    fun `when query contains special characters, should save query as is`() = runTest {

        val specialCharQuery = "iphone @#$% 15-pro"
        useCase(specialCharQuery)
        verify(repository).saveSearch(specialCharQuery)
    }

    @Test
    fun `when query contains numbers, should save query correctly`() = runTest {

        val numericQuery = "samsung s24 ultra 512gb"
        useCase(numericQuery)
        verify(repository).saveSearch(numericQuery)
    }

    @Test
    fun `when multiple saves are called, should call repository each time`() = runTest {

        val query1 = "first search"
        val query2 = "second search"
        useCase(query1)
        useCase(query2)
        verify(repository).saveSearch(query1)
        verify(repository).saveSearch(query2)
    }
}
