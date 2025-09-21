package com.products.app.data.repository

import com.google.common.truth.Truth.assertThat
import com.products.app.core.NetworkErrorHandler
import com.products.app.data.remote.AutosuggestApi
import com.products.app.data.remote.dto.SimpleAutosuggestDto
import com.products.app.data.remote.dto.SimpleSuggestedQueryDto
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
import org.mockito.kotlin.*
import java.io.IOException

@ExperimentalCoroutinesApi
class AutosuggestRepositoryImplTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var autosuggestApi: AutosuggestApi

    @Mock
    private lateinit var errorHandler: NetworkErrorHandler

    private lateinit var repository: AutosuggestRepositoryImpl

    @Before
    fun setUp() {
        repository = AutosuggestRepositoryImpl(autosuggestApi, errorHandler)
    }

    @Test
    fun `when api returns success, should return mapped suggestions`() = runTest {

        val query = "iphone"
        val apiResponse = SimpleAutosuggestDto(
            q = query,
            suggested_queries = listOf(
                SimpleSuggestedQueryDto(
                    q = "iphone 15",
                    match_start = 0,
                    match_end = 6,
                    is_verified_store = false,
                    filters = emptyList()
                )
            ),
            filter_logos = emptyList()
        )
        whenever(autosuggestApi.getAutosuggest(query)).thenReturn(apiResponse)
        val result = repository.getSuggestions(query)
        val suggestions = result.assertSuccess()
        assertThat(suggestions).hasSize(1)
        assertThat(suggestions[0].query).isEqualTo("iphone 15")
        assertThat(suggestions[0].matchStart).isEqualTo(0)
        assertThat(suggestions[0].matchEnd).isEqualTo(6)
        assertThat(suggestions[0].isVerifiedStore).isFalse()
        
        verify(autosuggestApi).getAutosuggest(query)
    }

    @Test
    fun `when query is empty, should return empty list`() = runTest {

        val emptyQuery = ""
        val result = repository.getSuggestions(emptyQuery)
        val suggestions = result.assertSuccess()
        assertThat(suggestions).isEmpty()
    }

    @Test
    fun `when query is blank with spaces, should return empty list`() = runTest {

        val blankQuery = "   "
        val result = repository.getSuggestions(blankQuery)
        val suggestions = result.assertSuccess()
        assertThat(suggestions).isEmpty()
    }

    @Test
    fun `when api returns empty suggestions, should return empty list`() = runTest {

        val query = "nonexistent"
        val apiResponse = SimpleAutosuggestDto(
            q = query,
            suggested_queries = emptyList(),
            filter_logos = emptyList()
        )
        whenever(autosuggestApi.getAutosuggest(query)).thenReturn(apiResponse)
        val result = repository.getSuggestions(query)
        val suggestions = result.assertSuccess()
        assertThat(suggestions).isEmpty()
    }

    @Test
    fun `when api returns null suggestions, should return empty list`() = runTest {

        val query = "test"
        val apiResponse = SimpleAutosuggestDto(
            q = query,
            suggested_queries = null,
            filter_logos = emptyList()
        )
        whenever(autosuggestApi.getAutosuggest(query)).thenReturn(apiResponse)
        val result = repository.getSuggestions(query)
        val suggestions = result.assertSuccess()
        assertThat(suggestions).isEmpty()
    }
    @Test
    fun `when api throws generic exception, should return error result`() = runTest {

        val query = "tablet"
        val exception = RuntimeException("Server error")
        whenever(autosuggestApi.getAutosuggest(query)).thenThrow(exception)
        whenever(errorHandler.handleError(exception)).thenReturn("Server error")
        val result = repository.getSuggestions(query)
        result.assertError("Server error")
        verify(errorHandler).handleError(exception)
    }

    @Test
    fun `repository should be properly initialized`() = runTest {

        assertThat(repository).isNotNull()
    }
}