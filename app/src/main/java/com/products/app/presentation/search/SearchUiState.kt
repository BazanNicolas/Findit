package com.products.app.presentation.search

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion
import com.products.app.domain.model.ViewedProduct

/**
 * UI state for the Search screen.
 * 
 * This data class represents the complete state of the search screen UI,
 * including search functionality, suggestions, history, and recently viewed products.
 * It follows the unidirectional data flow pattern where the UI state is immutable
 * and updated through ViewModel actions.
 * 
 * @property searchQuery Current search query entered by the user
 * @property suggestions List of search suggestions from the autocomplete API
 * @property searchHistory List of search history entries
 * @property recentViewedProducts List of recently viewed products
 * @property loadingSuggestions Whether suggestions are currently being loaded
 * @property showSuggestions Whether to display the suggestions dropdown
 * @property showSearchHistory Whether to display the search history dropdown
 * @property error Error message to display to the user (null if no error)
 */
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
