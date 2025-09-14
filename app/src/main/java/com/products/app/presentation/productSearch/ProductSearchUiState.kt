package com.products.app.presentation.productSearch

import com.products.app.domain.model.Paging
import com.products.app.domain.model.Product
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion

data class ProductSearchUiState(
    val query: String = "",
    val loading: Boolean = false,
    val loadingMore: Boolean = false,
    val error: String? = null,
    val paginationError: String? = null,
    val products: List<Product> = emptyList(),
    val paging: Paging? = null,
    val hasReachedEnd: Boolean = false,
    val isInitialLoad: Boolean = true,
    val suggestions: List<SearchSuggestion> = emptyList(),
    val showSuggestions: Boolean = false,
    val loadingSuggestions: Boolean = false,
    val searchHistory: List<SearchHistory> = emptyList(),
    val showSearchHistory: Boolean = false
)