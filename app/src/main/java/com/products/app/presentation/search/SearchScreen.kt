package com.products.app.presentation.search

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.products.app.presentation.search.components.SearchContent
import com.products.app.presentation.search.components.SearchTopBar

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
            onClear = viewModel::clearSearch,
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
            onProductClick = onProductClick
        )
    }
}

