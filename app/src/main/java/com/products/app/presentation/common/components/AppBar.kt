package com.products.app.presentation.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.products.app.R
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion
import com.products.app.presentation.common.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    showSearch: Boolean = true,
    suggestions: List<SearchSuggestion> = emptyList(),
    showSuggestions: Boolean = false,
    loadingSuggestions: Boolean = false,
    searchHistory: List<SearchHistory> = emptyList(),
    showSearchHistory: Boolean = false,
    onSuggestionClick: (String) -> Unit = {},
    onHistoryClick: (String) -> Unit = {},
    onSuggestionComplete: (String) -> Unit = {},
    onHistoryComplete: (String) -> Unit = {},
    onClearHistory: () -> Unit = {},
    onHideSuggestions: () -> Unit = {},
    onShowSearchHistory: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                if (showSearch) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = onSearchQueryChange,
                        onSearchClick = onSearchClick,
                        onFocusChange = { isFocused ->
                            if (isFocused && searchQuery.isBlank()) {
                                onShowSearchHistory()
                            } else if (!isFocused && searchQuery.isBlank()) {
                                onHideSuggestions()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(
                        text = if (title.isEmpty()) stringResource(R.string.products_title) else title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        
        if (showSearch && (showSuggestions || showSearchHistory)) {
            AutosuggestDropdown(
                suggestions = suggestions,
                loadingSuggestions = loadingSuggestions,
                searchHistory = searchHistory,
                onSuggestionClick = { query ->
                    onSuggestionClick(query)
                    focusManager.clearFocus()
                },
                onHistoryClick = { query ->
                    onHistoryClick(query)
                    focusManager.clearFocus()
                },
                onSuggestionComplete = { query ->
                    onSuggestionComplete(query)
                    focusManager.clearFocus()
                },
                onHistoryComplete = { query ->
                    onHistoryComplete(query)
                    focusManager.clearFocus()
                },
                onClearHistory = onClearHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}
