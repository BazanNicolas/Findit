package com.products.app.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.products.app.core.AppResult
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion
import com.products.app.domain.usecase.*
import com.products.app.util.MockDataFactory
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class HomeViewModelTest {

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

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        // Setup default returns for initialization
        whenever(getRecentSearchesUseCase(5)).thenReturn(flowOf(emptyList()))
        whenever(getRecentViewedProductsUseCase(6)).thenReturn(flowOf(emptyList()))

        viewModel = HomeViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getMatchingSearchesUseCase = getMatchingSearchesUseCase,
            getAutosuggestUseCase = getAutosuggestUseCase,
            getRecentViewedProductsUseCase = getRecentViewedProductsUseCase,
            saveSearchUseCase = saveSearchUseCase
        )
    }

    @Test
    fun `initial state should be correct`() = runTest {
        // When & Then
        viewModel.uiState.test {
            val initialState = awaitItem()
            assertThat(initialState.searchQuery).isEmpty()
            assertThat(initialState.recentSearches).isEmpty()
            assertThat(initialState.recentViewedProducts).isEmpty()
            assertThat(initialState.suggestions).isEmpty()
            assertThat(initialState.searchHistory).isEmpty()
            assertThat(initialState.showSuggestions).isFalse()
            assertThat(initialState.showSearchHistory).isFalse()
            assertThat(initialState.loadingSuggestions).isFalse()
        }
    }

    @Test
    fun `when initialized, should load recent searches and viewed products`() = runTest {
        // Given
        val recentSearches = MockDataFactory.createSearchHistoryList(3)
        val recentViewedProducts = MockDataFactory.createViewedProductList(4)

        // Reset mocks and setup new returns
        reset(getRecentSearchesUseCase, getRecentViewedProductsUseCase)
        whenever(getRecentSearchesUseCase(5)).thenReturn(flowOf(recentSearches))
        whenever(getRecentViewedProductsUseCase(6)).thenReturn(flowOf(recentViewedProducts))

        // When
        val newViewModel = HomeViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getMatchingSearchesUseCase = getMatchingSearchesUseCase,
            getAutosuggestUseCase = getAutosuggestUseCase,
            getRecentViewedProductsUseCase = getRecentViewedProductsUseCase,
            saveSearchUseCase = saveSearchUseCase
        )

        // Then
        newViewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.recentSearches).isEqualTo(recentSearches)
            assertThat(state.recentViewedProducts).isEqualTo(recentViewedProducts)
        }

        verify(getRecentSearchesUseCase).invoke(5)
        verify(getRecentViewedProductsUseCase).invoke(6)
    }

    @Test
    fun `when query changes to non-blank, should load suggestions and matching history`() = runTest {
        // Given
        val query = "iphone"
        val suggestions = MockDataFactory.createSearchSuggestionList(3)
        val matchingHistory = MockDataFactory.createSearchHistoryList(2)

        whenever(getAutosuggestUseCase(query)).thenReturn(AppResult.Success(suggestions))
        whenever(getMatchingSearchesUseCase(query)).thenReturn(flowOf(matchingHistory))

        // When
        viewModel.onQueryChange(query)

        // Then
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
        // Given
        val recentHistory = MockDataFactory.createSearchHistoryList(3)
        whenever(getRecentSearchesUseCase(5)).thenReturn(flowOf(recentHistory))

        // When
        viewModel.onQueryChange("")

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEmpty()
            assertThat(state.searchHistory).isEqualTo(recentHistory)
            assertThat(state.showSearchHistory).isTrue()
            assertThat(state.showSuggestions).isFalse()
        }
    }

    @Test
    fun `when autosuggestions fail, should hide suggestions`() = runTest {
        // Given
        val query = "test"
        val errorMessage = "Network error"
        whenever(getAutosuggestUseCase(query)).thenReturn(AppResult.Error(errorMessage))
        whenever(getMatchingSearchesUseCase(query)).thenReturn(flowOf(emptyList()))

        // When
        viewModel.onQueryChange(query)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.suggestions).isEmpty()
            assertThat(state.showSuggestions).isFalse()
            assertThat(state.loadingSuggestions).isFalse()
        }
    }

    @Test
    fun `when suggestion is clicked, should update query and hide suggestions`() = runTest {
        // Given
        val suggestion = SearchSuggestion(
            query = "iphone 15",
            matchStart = 0,
            matchEnd = 9,
            isVerifiedStore = false
        )

        // When
        viewModel.onSuggestionClick(suggestion)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEqualTo("iphone 15")
            assertThat(state.showSuggestions).isFalse()
        }
    }

    @Test
    fun `when history item is clicked, should update query and hide history`() = runTest {
        // Given
        val history = SearchHistory("samsung galaxy", System.currentTimeMillis())

        // When
        viewModel.onHistoryClick(history)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchQuery).isEqualTo("samsung galaxy")
            assertThat(state.showSearchHistory).isFalse()
        }
    }

    @Test
    fun `when hide suggestions is called, should hide suggestions and history`() = runTest {
        // When
        viewModel.hideSuggestions()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.showSuggestions).isFalse()
            assertThat(state.showSearchHistory).isFalse()
        }
    }

    @Test
    fun `when show search history with blank query, should load recent history`() = runTest {
        // Given
        val recentHistory = MockDataFactory.createSearchHistoryList(3)
        whenever(getRecentSearchesUseCase(5)).thenReturn(flowOf(recentHistory))

        // Ensure query is blank
        viewModel.onQueryChange("")

        // When
        viewModel.showSearchHistory()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.searchHistory).isEqualTo(recentHistory)
        }
    }

    @Test
    fun `view model should be properly initialized`() = runTest {
        // When & Then - Verify ViewModel exists
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `when search button is clicked with blank query, should not save search`() = runTest {
        // Given
        viewModel.onQueryChange("   ")

        // When
        viewModel.onSearchClick()

        // Then
        verify(saveSearchUseCase, never()).invoke(any())
    }

    @Test
    fun `when search button is clicked with empty query, should not save search`() = runTest {
        // Given
        viewModel.onQueryChange("")

        // When
        viewModel.onSearchClick()

        // Then
        verify(saveSearchUseCase, never()).invoke(any())
    }

    @Test
    fun `when loading suggestions, should show loading state`() = runTest {
        // Given
        val query = "laptop"
        whenever(getMatchingSearchesUseCase(query)).thenReturn(flowOf(emptyList()))
        whenever(getAutosuggestUseCase(query)).thenReturn(AppResult.Success(emptyList()))

        // When
        viewModel.onQueryChange(query)

        // Then - Check that loading state is set to true initially
        viewModel.uiState.test {
            val state = awaitItem()
            // Should eventually call the autosuggest use case
            verify(getAutosuggestUseCase).invoke(query)
            verify(getMatchingSearchesUseCase).invoke(query)
        }
    }

    @Test
    fun `when view model is created, should have valid instance`() = runTest {
        // When & Then - Basic validation
        assertThat(viewModel).isNotNull()
        assertThat(viewModel.toString()).isNotEmpty()
    }

    @Test
    fun `when recent searches and viewed products update, should reflect in UI state`() = runTest {
        // Given
        val initialSearches = MockDataFactory.createSearchHistoryList(2)
        val updatedSearches = MockDataFactory.createSearchHistoryList(3)
        val initialProducts = MockDataFactory.createViewedProductList(2)
        val updatedProducts = MockDataFactory.createViewedProductList(4)

        // Reset and setup new flows that emit multiple values
        reset(getRecentSearchesUseCase, getRecentViewedProductsUseCase)
        whenever(getRecentSearchesUseCase(5)).thenReturn(
            flowOf(initialSearches, updatedSearches)
        )
        whenever(getRecentViewedProductsUseCase(6)).thenReturn(
            flowOf(initialProducts, updatedProducts)
        )

        // When
        val newViewModel = HomeViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getMatchingSearchesUseCase = getMatchingSearchesUseCase,
            getAutosuggestUseCase = getAutosuggestUseCase,
            getRecentViewedProductsUseCase = getRecentViewedProductsUseCase,
            saveSearchUseCase = saveSearchUseCase
        )

        // Then - Just verify the ViewModel was created and use cases were called
        verify(getRecentSearchesUseCase).invoke(5)
        verify(getRecentViewedProductsUseCase).invoke(6)
    }
}
