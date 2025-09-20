package com.products.app.presentation.productSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.core.AppResult
import com.products.app.core.PaginationConstants
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.usecase.LoadMoreProductsUseCase
import com.products.app.domain.usecase.SaveSearchUseCase
import com.products.app.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Product Search screen that manages search results and pagination.
 * 
 * This ViewModel handles product search operations, including initial search,
 * pagination for infinite scrolling, and search history management. It coordinates
 * between multiple use cases to provide a seamless search experience.
 * 
 * The ViewModel follows the MVVM pattern and uses StateFlow to expose UI state
 * changes to the Compose UI. It manages loading states, error handling, and
 * pagination logic for infinite scrolling.
 * 
 * @param searchProductsUseCase Use case for performing product searches
 * @param loadMoreProductsUseCase Use case for loading additional search results
 * @param saveSearchUseCase Use case for saving search queries to history
 */
@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val loadMoreProductsUseCase: LoadMoreProductsUseCase,
    private val saveSearchUseCase: SaveSearchUseCase
) : ViewModel() {

    private val _ui = MutableStateFlow(ProductSearchUiState())
    val ui: StateFlow<ProductSearchUiState> = _ui

    /**
     * Updates the search query in the UI state.
     * 
     * @param q The new search query text
     */
    fun onQueryChange(q: String) {
        _ui.update { 
            it.copy(query = q) 
        }
    }

    /**
     * Performs the initial search for products.
     * 
     * This method handles the first page of search results, including loading state
     * management, error handling, and saving the search query to history.
     * 
     * @param limit Maximum number of products to return (defaults to PaginationConstants.DEFAULT_PAGE_SIZE)
     */
    fun searchFirstPage(limit: Int = PaginationConstants.DEFAULT_PAGE_SIZE) = viewModelScope.launch {
        val q = ui.value.query.trim()
        if (q.isEmpty()) return@launch

        _ui.update { 
            it.copy(
                loading = true, 
                error = null, 
                paginationError = null,
                isInitialLoad = true
            ) 
        }

        when (val res = searchProductsUseCase(query = q, offset = 0, limit = limit)) {
            is AppResult.Success -> {
                saveSearchUseCase(q)
                applyResult(firstPage = true, res = res.data)
            }
            is AppResult.Error -> _ui.update { 
                it.copy(
                    loading = false, 
                    error = res.message,
                    isInitialLoad = false
                ) 
            }
        }
    }

    fun loadNextPage() = viewModelScope.launch {
        val current = ui.value
        val q = current.query.trim()
        val paging = current.paging ?: return@launch

        if (current.loadingMore || current.hasReachedEnd || q.isEmpty()) return@launch

        paging.offset + paging.limit

        _ui.update { 
            it.copy(
                loadingMore = true, 
                paginationError = null
            ) 
        }

        when (val res = loadMoreProductsUseCase(query = q, currentOffset = paging.offset, limit = paging.limit)) {
            is AppResult.Success -> applyResult(firstPage = false, res = res.data)
            is AppResult.Error -> _ui.update { 
                it.copy(
                    loadingMore = false, 
                    paginationError = res.message
                ) 
            }
        }
    }

    private fun applyResult(firstPage: Boolean, res: ProductSearchResult) {
        _ui.update {
            val newList = if (firstPage) res.products else it.products + res.products
            val hasReachedEnd = res.products.isEmpty()
            
            it.copy(
                loading = false,
                loadingMore = false,
                products = newList,
                paging = res.paging,
                hasReachedEnd = hasReachedEnd,
                error = null,
                paginationError = null,
                isInitialLoad = false
            )
        }
    }
    
    fun retryPagination() = viewModelScope.launch {
        loadNextPage()
    }
    
    fun clearError() {
        _ui.update { it.copy(error = null, paginationError = null) }
    }
}
