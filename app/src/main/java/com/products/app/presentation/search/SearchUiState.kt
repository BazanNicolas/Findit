package com.products.app.presentation.search

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion
import com.products.app.domain.model.ViewedProduct

data class SearchUiState(
    val searchQuery: String = "",
    val suggestions: List<SearchSuggestion> = emptyList(),
    val searchHistory: List<SearchHistory> = emptyList(),
    val recentViewedProducts: List<ViewedProduct> = emptyList(),
    val loadingSuggestions: Boolean = false,
    val showSuggestions: Boolean = false,
    val showSearchHistory: Boolean = false,
    val error: String? = null
)
