package com.products.app.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.products.app.core.AppResult
import com.products.app.domain.repository.AutosuggestRepository
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
class GetAutosuggestUseCaseTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: AutosuggestRepository

    private lateinit var useCase: GetAutosuggestUseCase

    @Before
    fun setUp() {
        useCase = GetAutosuggestUseCase(repository)
    }

    @Test
    fun `when query is valid, should return suggestions successfully`() = runTest {

        val query = "iphone"
        val expectedSuggestions = MockDataFactory.createSearchSuggestionList(3)
        whenever(repository.getSuggestions(query)).thenReturn(AppResult.Success(expectedSuggestions))
        val result = useCase(query)
        val actualSuggestions = result.assertSuccess()
        assertThat(actualSuggestions).isEqualTo(expectedSuggestions)
        assertThat(actualSuggestions).hasSize(3)
        verify(repository).getSuggestions(query)
    }

    @Test
    fun `when query has leading and trailing spaces, should trim query`() = runTest {

        val queryWithSpaces = "  samsung  "
        val trimmedQuery = "samsung"
        val expectedSuggestions = MockDataFactory.createSearchSuggestionList(2)
        whenever(repository.getSuggestions(trimmedQuery)).thenReturn(AppResult.Success(expectedSuggestions))
        val result = useCase(queryWithSpaces)
        result.assertSuccess()
        verify(repository).getSuggestions(trimmedQuery)
    }

    @Test
    fun `when repository returns error, should return error result`() = runTest {

        val query = "laptop"
        val errorMessage = "Network connection failed"
        whenever(repository.getSuggestions(query)).thenReturn(AppResult.Error(errorMessage))
        val result = useCase(query)
        result.assertError(errorMessage)
        verify(repository).getSuggestions(query)
    }

    @Test
    fun `when query is empty, should return empty suggestions`() = runTest {

        val emptyQuery = ""
        val emptySuggestions = emptyList<com.products.app.domain.model.SearchSuggestion>()
        whenever(repository.getSuggestions(emptyQuery)).thenReturn(AppResult.Success(emptySuggestions))
        val result = useCase(emptyQuery)
        val actualSuggestions = result.assertSuccess()
        assertThat(actualSuggestions).isEmpty()
        verify(repository).getSuggestions(emptyQuery)
    }

    @Test
    fun `when query is single character, should return suggestions`() = runTest {

        val singleCharQuery = "a"
        val expectedSuggestions = listOf(
            MockDataFactory.createSearchSuggestion("apple"),
            MockDataFactory.createSearchSuggestion("android")
        )
        whenever(repository.getSuggestions(singleCharQuery)).thenReturn(AppResult.Success(expectedSuggestions))
        val result = useCase(singleCharQuery)
        val actualSuggestions = result.assertSuccess()
        assertThat(actualSuggestions).hasSize(2)
        assertThat(actualSuggestions[0].query).isEqualTo("apple")
        assertThat(actualSuggestions[1].query).isEqualTo("android")
    }

    @Test
    fun `when query contains special characters, should handle correctly`() = runTest {

        val specialCharQuery = "iphone-15"
        val expectedSuggestions = MockDataFactory.createSearchSuggestionList(1)
        whenever(repository.getSuggestions(specialCharQuery)).thenReturn(AppResult.Success(expectedSuggestions))
        val result = useCase(specialCharQuery)
        result.assertSuccess()
        verify(repository).getSuggestions(specialCharQuery)
    }

    @Test
    fun `when query is very long, should still process correctly`() = runTest {

        val longQuery = "this is a very long search query that might be used by a user"
        val expectedSuggestions = MockDataFactory.createSearchSuggestionList(1)
        whenever(repository.getSuggestions(longQuery)).thenReturn(AppResult.Success(expectedSuggestions))
        val result = useCase(longQuery)
        result.assertSuccess()
        verify(repository).getSuggestions(longQuery)
    }

    @Test
    fun `when repository returns no suggestions, should return empty list`() = runTest {

        val query = "xyzunknownproduct"
        val noSuggestions = emptyList<com.products.app.domain.model.SearchSuggestion>()
        whenever(repository.getSuggestions(query)).thenReturn(AppResult.Success(noSuggestions))
        val result = useCase(query)
        val actualSuggestions = result.assertSuccess()
        assertThat(actualSuggestions).isEmpty()
    }

    @Test
    fun `when query contains numbers, should return relevant suggestions`() = runTest {

        val numericQuery = "iphone 15"
        val expectedSuggestions = listOf(
            MockDataFactory.createSearchSuggestion("iphone 15 pro"),
            MockDataFactory.createSearchSuggestion("iphone 15 pro max")
        )
        whenever(repository.getSuggestions(numericQuery)).thenReturn(AppResult.Success(expectedSuggestions))
        val result = useCase(numericQuery)
        val actualSuggestions = result.assertSuccess()
        assertThat(actualSuggestions).hasSize(2)
        assertThat(actualSuggestions[0].query).contains("iphone 15")
        assertThat(actualSuggestions[1].query).contains("iphone 15")
    }
}
