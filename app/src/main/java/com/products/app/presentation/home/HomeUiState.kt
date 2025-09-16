package com.products.app.presentation.home

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion
import com.products.app.domain.model.ViewedProduct

data class HomeUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val suggestions: List<SearchSuggestion> = emptyList(),
    val showSuggestions: Boolean = false,
    val loadingSuggestions: Boolean = false,
    val searchHistory: List<SearchHistory> = emptyList(),
    val showSearchHistory: Boolean = false,
    val recentViewedProducts: List<ViewedProduct> = emptyList(),
    val recentSearches: List<SearchHistory> = emptyList()
)
