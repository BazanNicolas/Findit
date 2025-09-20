package com.products.app.presentation.home

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.ViewedProduct

/**
 * UI state for the Home screen.
 * 
 * This data class represents the complete state of the home screen UI,
 * including recently viewed products and recent searches.
 * It follows the unidirectional data flow pattern where the UI state is immutable
 * and updated through ViewModel actions.
 * 
 * @property isLoading Whether the screen is in a loading state
 * @property error Error message to display to the user (null if no error)
 * @property recentViewedProducts List of recently viewed products
 * @property recentSearches List of recent search queries
 */
data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val recentViewedProducts: List<ViewedProduct> = emptyList(),
    val recentSearches: List<SearchHistory> = emptyList()
)
