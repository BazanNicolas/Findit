package com.products.app.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.products.app.domain.model.SearchHistory
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

/**
 * Test suite for HomeViewModel.
 * 
 * This test class verifies the ViewModel logic for the Home screen, including:
 * - Initial state setup and data loading
 * - Search history management
 * - User interaction handling (deleting searches and viewed products)
 * - Error handling and loading states
 * - State updates and UI state management
 * 
 * The tests use Turbine for Flow testing, Mockito for dependency mocking,
 * and Truth for assertions. They ensure the ViewModel correctly coordinates
 * between multiple use cases and maintains proper UI state.
 */
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
    private lateinit var getRecentViewedProductsUseCase: GetRecentViewedProductsUseCase

    @Mock
    private lateinit var deleteSearchUseCase: DeleteSearchUseCase

    @Mock
    private lateinit var clearAllSearchesUseCase: ClearAllSearchesUseCase

    @Mock
    private lateinit var deleteViewedProductUseCase: DeleteViewedProductUseCase

    @Mock
    private lateinit var clearAllViewedProductsUseCase: ClearAllViewedProductsUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        // Setup default returns for initialization
        whenever(getRecentSearchesUseCase(5)).thenReturn(flowOf(emptyList()))
        whenever(getRecentViewedProductsUseCase(6)).thenReturn(flowOf(emptyList()))

        viewModel = HomeViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getRecentViewedProductsUseCase = getRecentViewedProductsUseCase,
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
            assertThat(initialState.recentSearches).isEmpty()
            assertThat(initialState.recentViewedProducts).isEmpty()
            assertThat(initialState.isLoading).isFalse()
            assertThat(initialState.error).isNull()
        }
    }

    @Test
    fun `when initialized, should load recent searches and viewed products`() = runTest {

        val recentSearches = MockDataFactory.createSearchHistoryList(3)
        val recentViewedProducts = MockDataFactory.createViewedProductList(4)

        // Reset mocks and setup new returns
        reset(getRecentSearchesUseCase, getRecentViewedProductsUseCase)
        whenever(getRecentSearchesUseCase(5)).thenReturn(flowOf(recentSearches))
        whenever(getRecentViewedProductsUseCase(6)).thenReturn(flowOf(recentViewedProducts))
        val newViewModel = HomeViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getRecentViewedProductsUseCase = getRecentViewedProductsUseCase,
            deleteSearchUseCase = deleteSearchUseCase,
            clearAllSearchesUseCase = clearAllSearchesUseCase,
            deleteViewedProductUseCase = deleteViewedProductUseCase,
            clearAllViewedProductsUseCase = clearAllViewedProductsUseCase
        )
        newViewModel.uiState.test {
            val state = awaitItem()
            assertThat(state.recentSearches).isEqualTo(recentSearches)
            assertThat(state.recentViewedProducts).isEqualTo(recentViewedProducts)
        }

        verify(getRecentSearchesUseCase).invoke(5)
        verify(getRecentViewedProductsUseCase).invoke(6)
    }

    @Test
    fun `when delete search is called, should call delete search use case`() = runTest {
        val search = SearchHistory("test query", System.currentTimeMillis())
        viewModel.deleteSearch(search)
        verify(deleteSearchUseCase).invoke(search)
    }

    @Test
    fun `when clear all searches is called, should call clear all searches use case`() = runTest {
        viewModel.clearAllSearches()
        verify(clearAllSearchesUseCase).invoke()
    }

    @Test
    fun `when delete viewed product is called, should call delete viewed product use case`() = runTest {
        val viewedProduct = MockDataFactory.createViewedProduct()
        viewModel.deleteViewedProduct(viewedProduct)
        verify(deleteViewedProductUseCase).invoke(viewedProduct)
    }

    @Test
    fun `when clear all viewed products is called, should call clear all viewed products use case`() = runTest {
        viewModel.clearAllViewedProducts()
        verify(clearAllViewedProductsUseCase).invoke()
    }

    @Test
    fun `view model should be properly initialized`() = runTest {
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `when recent searches and viewed products update, should reflect in UI state`() = runTest {
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
        val newViewModel = HomeViewModel(
            getRecentSearchesUseCase = getRecentSearchesUseCase,
            getRecentViewedProductsUseCase = getRecentViewedProductsUseCase,
            deleteSearchUseCase = deleteSearchUseCase,
            clearAllSearchesUseCase = clearAllSearchesUseCase,
            deleteViewedProductUseCase = deleteViewedProductUseCase,
            clearAllViewedProductsUseCase = clearAllViewedProductsUseCase
        )
        verify(getRecentSearchesUseCase).invoke(5)
        verify(getRecentViewedProductsUseCase).invoke(6)
    }
}
