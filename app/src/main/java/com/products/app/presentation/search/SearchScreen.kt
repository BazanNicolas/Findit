package com.products.app.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.products.app.presentation.search.components.SearchContent
import com.products.app.presentation.search.components.SearchTopBar

/**
 * Composable screen that provides search functionality with autocomplete and history.
 * 
 * This screen serves as the main search interface and includes:
 * - Search bar with real-time autocomplete suggestions
 * - Recent search history display
 * - Recently viewed products section
 * - Search suggestions from MercadoLibre API
 * - Management of search and viewing history
 * 
 * The screen automatically loads recent searches and viewed products on initialization
 * and provides reactive updates as the user types. It handles both local search
 * history and remote API suggestions for a comprehensive search experience.
 * 
 * @param onBackClick Callback invoked when the back button is pressed
 * @param onSearchClick Callback invoked when a search is performed
 * @param onProductClick Callback invoked when a product is selected
 * @param viewModel The SearchViewModel instance for managing state
 */
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onSearchClick: (String) -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchTopBar(
            query = uiState.searchQuery,
            onQueryChange = viewModel::onQueryChange,
            onSearch = { 
                viewModel.onSearchClick()
                onSearchClick(uiState.searchQuery)
            },
            onClear = { viewModel.clearSearch() },
            onBackClick = onBackClick
        )
        
        SearchContent(
            uiState = uiState,
            onHistoryClick = { history ->
                viewModel.onHistoryClick(history)
                onSearchClick(history.query)
            },
            onSuggestionClick = { suggestion ->
                viewModel.onSuggestionClick(suggestion)
                onSearchClick(suggestion.query)
            },
            onProductClick = onProductClick,
            onDeleteSearch = { search -> viewModel.deleteSearch(search) },
            onClearAllSearches = { viewModel.clearAllSearches() },
            onDeleteViewedProduct = { product -> viewModel.deleteViewedProduct(product) },
            onClearAllViewedProducts = { viewModel.clearAllViewedProducts() }
        )
    }
}

