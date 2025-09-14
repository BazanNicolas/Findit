package com.products.app.presentation.productSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.core.AppResult
import com.products.app.core.PaginationConstants
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.usecase.SearchProductsUseCase
import com.products.app.domain.usecase.LoadMoreProductsUseCase
import com.products.app.domain.usecase.GetAutosuggestUseCase
import com.products.app.presentation.productSearch.ProductSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val loadMoreProductsUseCase: LoadMoreProductsUseCase,
    private val getAutosuggestUseCase: GetAutosuggestUseCase
) : ViewModel() {

    private val _ui = MutableStateFlow(ProductSearchUiState())
    val ui: StateFlow<ProductSearchUiState> = _ui

    fun onQueryChange(q: String) {
        _ui.update { 
            it.copy(
                query = q,
                showSuggestions = q.isNotBlank() && !it.loading
            ) 
        }
        
        if (q.isNotBlank()) {
            loadSuggestions(q)
        } else {
            _ui.update { 
                it.copy(
                    suggestions = emptyList(), 
                    showSuggestions = false,
                    loadingSuggestions = false
                ) 
            }
        }
    }

    private fun loadSuggestions(query: String) = viewModelScope.launch {
        if (query.length < 2) {
            _ui.update { 
                it.copy(
                    suggestions = emptyList(),
                    loadingSuggestions = false,
                    showSuggestions = it.query.isNotBlank() && !it.loading
                ) 
            }
            return@launch
        }
        
        _ui.update { it.copy(loadingSuggestions = true) }
        
        when (val result = getAutosuggestUseCase(query)) {
            is AppResult.Success -> {
                _ui.update { 
                    it.copy(
                        suggestions = result.data,
                        loadingSuggestions = false,
                        showSuggestions = it.query.isNotBlank() && !it.loading
                    ) 
                }
            }
            is AppResult.Error -> {
                _ui.update { 
                    it.copy(
                        suggestions = emptyList(),
                        loadingSuggestions = false,
                        showSuggestions = it.query.isNotBlank() && !it.loading
                    ) 
                }
            }
        }
    }

    fun onSuggestionClick(query: String) {
        _ui.update { 
            it.copy(
                query = query,
                showSuggestions = false,
                suggestions = emptyList()
            ) 
        }
        searchFirstPage()
    }

    fun hideSuggestions() {
        _ui.update { it.copy(showSuggestions = false) }
    }

    fun searchFirstPage(limit: Int = PaginationConstants.DEFAULT_PAGE_SIZE) = viewModelScope.launch {
        val q = ui.value.query.trim()
        if (q.isEmpty()) return@launch

        _ui.update { 
            it.copy(
                loading = true, 
                error = null, 
                paginationError = null,
                isInitialLoad = true,
                showSuggestions = false,
                suggestions = emptyList()
            ) 
        }

        when (val res = searchProductsUseCase(query = q, offset = 0, limit = limit)) {
            is AppResult.Success -> applyResult(firstPage = true, res = res.data)
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
        
        val nextOffset = paging.offset + paging.limit

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
