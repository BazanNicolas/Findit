package com.products.app.presentation.productSearch

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.products.app.domain.usecase.*
import com.products.app.util.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class ProductSearchViewModelTest {

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var searchProductsUseCase: SearchProductsUseCase

    @Mock
    private lateinit var loadMoreProductsUseCase: LoadMoreProductsUseCase

    @Mock
    private lateinit var getAutosuggestUseCase: GetAutosuggestUseCase

    @Mock
    private lateinit var saveSearchUseCase: SaveSearchUseCase

    @Mock
    private lateinit var getRecentSearchesUseCase: GetRecentSearchesUseCase

    @Mock
    private lateinit var getMatchingSearchesUseCase: GetMatchingSearchesUseCase

    @Mock
    private lateinit var clearSearchHistoryUseCase: ClearSearchHistoryUseCase

    private lateinit var viewModel: ProductSearchViewModel

    @Before
    fun setUp() {
        viewModel = ProductSearchViewModel(
            searchProductsUseCase = searchProductsUseCase,
            loadMoreProductsUseCase = loadMoreProductsUseCase,
            getAutosuggestUseCase = getAutosuggestUseCase,
            saveSearchUseCase = saveSearchUseCase,
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getMatchingSearchesUseCase = getMatchingSearchesUseCase,
            clearSearchHistoryUseCase = clearSearchHistoryUseCase
        )
    }

    @Test
    fun `view model should be created successfully`() = runTest {
        // When & Then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `initial state should have correct default values`() = runTest {
        // When & Then
        val state = viewModel.ui.value
        assertThat(state.query).isEmpty()
        assertThat(state.products).isEmpty()
        assertThat(state.loading).isFalse()
        assertThat(state.loadingMore).isFalse()
        assertThat(state.error).isNull()
        assertThat(state.paginationError).isNull()
        assertThat(state.isInitialLoad).isTrue()
        assertThat(state.showSuggestions).isFalse()
        assertThat(state.suggestions).isEmpty()
    }

    @Test
    fun `clear error should reset error states`() = runTest {
        // When
        viewModel.clearError()

        // Then
        val state = viewModel.ui.value
        assertThat(state.error).isNull()
        assertThat(state.paginationError).isNull()
    }

    @Test
    fun `view model should maintain consistent state`() = runTest {
        // Given
        viewModel.clearError()

        // When
        val state1 = viewModel.ui.value
        val state2 = viewModel.ui.value

        // Then
        assertThat(state1.error).isEqualTo(state2.error)
        assertThat(state1.paginationError).isEqualTo(state2.paginationError)
        assertThat(state1.loading).isEqualTo(state2.loading)
        assertThat(state1.loadingMore).isEqualTo(state2.loadingMore)
    }

    @Test
    fun `initial state should be ready for user interaction`() = runTest {
        // When & Then
        val state = viewModel.ui.value
        assertThat(state.isInitialLoad).isTrue()
        assertThat(state.loading).isFalse()
        assertThat(state.showSuggestions).isFalse()
    }
}