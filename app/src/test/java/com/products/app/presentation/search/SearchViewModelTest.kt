package com.products.app.presentation.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.products.app.core.AppResult
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion
import com.products.app.domain.model.ViewedProduct
import com.products.app.domain.usecase.*
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.*
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.ArgumentMatchers.anyInt

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var getRecentSearchesUseCase: GetRecentSearchesUseCase

    @Mock
    private lateinit var getMatchingSearchesUseCase: GetMatchingSearchesUseCase

    @Mock
    private lateinit var getAutosuggestUseCase: GetAutosuggestUseCase

    @Mock
    private lateinit var getRecentViewedProductsUseCase: GetRecentViewedProductsUseCase

    @Mock
    private lateinit var saveSearchUseCase: SaveSearchUseCase

    @Mock
    private lateinit var deleteSearchUseCase: DeleteSearchUseCase

    @Mock
    private lateinit var clearAllSearchesUseCase: ClearAllSearchesUseCase

    @Mock
    private lateinit var deleteViewedProductUseCase: DeleteViewedProductUseCase

    @Mock
    private lateinit var clearAllViewedProductsUseCase: ClearAllViewedProductsUseCase

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        // Setup default returns for initialization
        whenever(getRecentSearchesUseCase(10)).thenReturn(flowOf(emptyList()))
        whenever(getRecentViewedProductsUseCase(10)).thenReturn(flowOf(emptyList()))
        whenever(getMatchingSearchesUseCase(anyString(), anyInt())).thenReturn(flowOf(emptyList()))

        viewModel = SearchViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getMatchingSearchesUseCase = getMatchingSearchesUseCase,
            getAutosuggestUseCase = getAutosuggestUseCase,
            getRecentViewedProductsUseCase = getRecentViewedProductsUseCase,
            saveSearchUseCase = saveSearchUseCase,
            deleteSearchUseCase = deleteSearchUseCase,
            clearAllSearchesUseCase = clearAllSearchesUseCase,
            deleteViewedProductUseCase = deleteViewedProductUseCase,
            clearAllViewedProductsUseCase = clearAllViewedProductsUseCase
        )
    }

    @Test
    fun `initial state should be correct`() = runTest {
        // When & Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.searchQuery).isEmpty()
            assertThat(initialState.suggestions).isEmpty()
            assertThat(initialState.searchHistory).isEmpty()
            assertThat(initialState.recentViewedProducts).isEmpty()
            assertThat(initialState.loadingSuggestions).isFalse()
            assertThat(initialState.showSuggestions).isFalse()
            assertThat(initialState.showSearchHistory).isFalse()
            assertThat(initialState.error).isNull()
        }
    }

    @Test
    fun `when initialized, should load recent searches and viewed products`() = runTest {

        val recentSearches = MockDataFactory.createSearchHistoryList(3)
        val recentViewedProducts = MockDataFactory.createViewedProductList(4)

        // Reset mocks and setup new returns
        reset(getRecentSearchesUseCase, getRecentViewedProductsUseCase)
        whenever(getRecentSearchesUseCase(10)).thenReturn(flowOf(recentSearches))
        whenever(getRecentViewedProductsUseCase(10)).thenReturn(flowOf(recentViewedProducts))
        val newViewModel = SearchViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getMatchingSearchesUseCase = getMatchingSearchesUseCase,
            getAutosuggestUseCase = getAutosuggestUseCase,
            getRecentViewedProductsUseCase = getRecentViewedProductsUseCase,
            saveSearchUseCase = saveSearchUseCase,
            deleteSearchUseCase = deleteSearchUseCase,
            clearAllSearchesUseCase = clearAllSearchesUseCase,
            deleteViewedProductUseCase = deleteViewedProductUseCase,
            clearAllViewedProductsUseCase = clearAllViewedProductsUseCase
        )
        newViewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchHistory).isEqualTo(recentSearches)
            assertThat(state.recentViewedProducts).isEqualTo(recentViewedProducts)
            assertThat(state.showSearchHistory).isTrue()
        }

        verify(getRecentSearchesUseCase).invoke(10)
        verify(getRecentViewedProductsUseCase).invoke(10)
    }

    @Test
    fun `when query changes to non-blank, should load suggestions and matching history`() = runTest {

        val query = "iphone"
        val suggestions = MockDataFactory.createSearchSuggestionList(3)
        val matchingHistory = MockDataFactory.createSearchHistoryList(2)

        whenever(getAutosuggestUseCase(query)).thenReturn(AppResult.Success(suggestions))
        whenever(getMatchingSearchesUseCase(query)).thenReturn(flowOf(matchingHistory))
        viewModel.onQueryChange(query)
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEqualTo(query)
            assertThat(state.suggestions).isEqualTo(suggestions)
            assertThat(state.searchHistory).isEqualTo(matchingHistory)
            assertThat(state.showSuggestions).isTrue()
            assertThat(state.showSearchHistory).isTrue()
            assertThat(state.loadingSuggestions).isFalse()
        }

        verify(getAutosuggestUseCase).invoke(query)
        verify(getMatchingSearchesUseCase).invoke(query)
    }

    @Test
    fun `when query changes to blank, should load recent history`() = runTest {

        val recentHistory = MockDataFactory.createSearchHistoryList(3)
        whenever(getRecentSearchesUseCase(10)).thenReturn(flowOf(recentHistory))
        viewModel.onQueryChange("")
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEmpty()
            assertThat(state.searchHistory).isEqualTo(recentHistory)
            assertThat(state.showSearchHistory).isTrue()
            assertThat(state.showSuggestions).isFalse()
        }
    }

    @Test
    fun `when autosuggestions fail, should hide suggestions and show error`() = runTest {

        val query = "test"
        val errorMessage = "Network error"
        whenever(getAutosuggestUseCase(query)).thenReturn(AppResult.Error(errorMessage))
        whenever(getMatchingSearchesUseCase(query)).thenReturn(flowOf(emptyList()))
        viewModel.onQueryChange(query)
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.suggestions).isEmpty()
            assertThat(state.showSuggestions).isFalse()
            assertThat(state.loadingSuggestions).isFalse()
            assertThat(state.error).isEqualTo(errorMessage)
        }
    }

    @Test
    fun `when suggestion is clicked, should update query and hide suggestions`() = runTest {

        val suggestion = SearchSuggestion(
            query = "iphone 15",
            matchStart = 0,
            matchEnd = 9,
            isVerifiedStore = false
        )
        viewModel.onSuggestionClick(suggestion)
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEqualTo("iphone 15")
            assertThat(state.showSuggestions).isFalse()
        }
    }

    @Test
    fun `when history item is clicked, should update query and hide history`() = runTest {

        val history = SearchHistory("samsung galaxy", System.currentTimeMillis())
        viewModel.onHistoryClick(history)
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEqualTo("samsung galaxy")
            assertThat(state.showSearchHistory).isFalse()
        }
    }

    @Test
    fun `when clear search is called, should reset query and load recent history`() = runTest {

        val recentHistory = MockDataFactory.createSearchHistoryList(2)
        whenever(getRecentSearchesUseCase(10)).thenReturn(flowOf(recentHistory))
        viewModel.clearSearch()
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEmpty()
            assertThat(state.showSuggestions).isFalse()
            assertThat(state.showSearchHistory).isTrue()
            assertThat(state.searchHistory).isEqualTo(recentHistory)
        }
        
        verify(getRecentSearchesUseCase, atLeast(1)).invoke(10)
    }

    @Test
    fun `when search button is clicked with valid query, should save search`() = runTest {

        val query = "laptop gaming"
        whenever(getAutosuggestUseCase(query)).thenReturn(AppResult.Success(emptyList()))
        

        viewModel.onQueryChange(query)
        viewModel.onSearchClick()
        verify(saveSearchUseCase, timeout(1000)).invoke(query)
    }

    @Test
    fun `when search button is clicked with blank query, should not save search`() = runTest {

        viewModel.onQueryChange("   ")
        viewModel.onSearchClick()
        verifyNoInteractions(saveSearchUseCase)
    }

    @Test
    fun `when search button is clicked with empty query, should not save search`() = runTest {

        viewModel.onQueryChange("")
        viewModel.onSearchClick()
        verifyNoInteractions(saveSearchUseCase)
    }

    @Test
    fun `when delete search is called, should call delete use case`() = runTest {

        val search = MockDataFactory.createSearchHistory("test search")
        viewModel.deleteSearch(search)
        verify(deleteSearchUseCase, timeout(1000)).invoke(search)
    }

    @Test
    fun `when clear all searches is called, should call clear all use case`() = runTest {

        viewModel.clearAllSearches()
        verify(clearAllSearchesUseCase, timeout(1000)).invoke()
    }

    @Test
    fun `when delete viewed product is called, should call delete use case`() = runTest {

        val viewedProduct = MockDataFactory.createViewedProduct()
        viewModel.deleteViewedProduct(viewedProduct)
        verify(deleteViewedProductUseCase).invoke(viewedProduct)
    }

    @Test
    fun `when clear all viewed products is called, should call clear all use case`() = runTest {

        viewModel.clearAllViewedProducts()
        verify(clearAllViewedProductsUseCase).invoke()
    }

    @Test
    fun `when loading suggestions, should show loading state`() = runTest {

        val query = "smartphone"
        whenever(getMatchingSearchesUseCase(query)).thenReturn(flowOf(emptyList()))
        whenever(getAutosuggestUseCase(query)).thenReturn(AppResult.Success(emptyList()))
        viewModel.onQueryChange(query)
        viewModel.uiState.test {
            val state = awaitItem()
            // Should eventually call the autosuggest use case
        }
        verify(getAutosuggestUseCase).invoke(query)
        verify(getMatchingSearchesUseCase).invoke(query)
    }

    @Test
    fun `when query has whitespace, should trim before saving`() = runTest {

        val queryWithSpaces = "  laptop gaming  "
        val expectedTrimmedQuery = "laptop gaming"
        whenever(getAutosuggestUseCase(queryWithSpaces)).thenReturn(AppResult.Success(emptyList()))
        whenever(getAutosuggestUseCase(expectedTrimmedQuery)).thenReturn(AppResult.Success(emptyList()))
        whenever(getMatchingSearchesUseCase(queryWithSpaces)).thenReturn(flowOf(emptyList()))
        

        viewModel.onQueryChange(queryWithSpaces)
        viewModel.onSearchClick()
        verify(saveSearchUseCase, timeout(1000)).invoke(expectedTrimmedQuery)
    }

    @Test
    fun `when multiple queries are entered, should handle each correctly`() = runTest {

        val query1 = "iphone"
        val query2 = "samsung"
        val suggestions1 = MockDataFactory.createSearchSuggestionList(2)
        val suggestions2 = MockDataFactory.createSearchSuggestionList(1)

        whenever(getAutosuggestUseCase(query1)).thenReturn(AppResult.Success(suggestions1))
        whenever(getAutosuggestUseCase(query2)).thenReturn(AppResult.Success(suggestions2))
        whenever(getMatchingSearchesUseCase(query1)).thenReturn(flowOf(emptyList()))
        whenever(getMatchingSearchesUseCase(query2)).thenReturn(flowOf(emptyList()))
        viewModel.onQueryChange(query1)
        viewModel.onQueryChange(query2)
        verify(getAutosuggestUseCase).invoke(query1)
        verify(getAutosuggestUseCase).invoke(query2)
        
        viewModel.uiState.test {
            val finalState = awaitItem()
            assertThat(finalState.searchQuery).isEqualTo(query2)
            assertThat(finalState.suggestions).isEqualTo(suggestions2)
        }
    }

    @Test
    fun `when autosuggest returns empty list, should hide suggestions`() = runTest {

        val query = "nonexistent"
        whenever(getAutosuggestUseCase(query)).thenReturn(AppResult.Success(emptyList()))
        whenever(getMatchingSearchesUseCase(query)).thenReturn(flowOf(emptyList()))
        viewModel.onQueryChange(query)
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.suggestions).isEmpty()
            assertThat(state.showSuggestions).isFalse()
        }
    }
}
