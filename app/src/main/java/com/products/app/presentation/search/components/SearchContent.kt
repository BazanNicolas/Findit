package com.products.app.presentation.search.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.products.app.R
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.model.SearchSuggestion
import com.products.app.domain.model.ViewedProduct

@Composable
fun SearchContent(
    uiState: com.products.app.presentation.search.SearchUiState,
    onHistoryClick: (SearchHistory) -> Unit,
    onSuggestionClick: (SearchSuggestion) -> Unit,
    onProductClick: (String) -> Unit,
    onDeleteSearch: (SearchHistory) -> Unit,
    onClearAllSearches: () -> Unit,
    onDeleteViewedProduct: (ViewedProduct) -> Unit,
    onClearAllViewedProducts: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (uiState.loadingSuggestions) {
            item {
                LoadingSuggestionsItem()
            }
        } else {
            if (uiState.searchHistory.isNotEmpty()) {
                item {
                    SectionHeaderWithClearButton(
                        title = stringResource(R.string.recent_searches),
                        onClearAll = onClearAllSearches
                    )
                }
                
                items(uiState.searchHistory) { history ->
                    SearchHistoryItem(
                        history = history,
                        onClick = { onHistoryClick(history) },
                        onDelete = { onDeleteSearch(history) }
                    )
                }
            }
            
            if (uiState.suggestions.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = stringResource(R.string.suggestions)
                    )
                }
                
                items(uiState.suggestions) { suggestion ->
                    SearchSuggestionItem(
                        suggestion = suggestion,
                        onClick = { onSuggestionClick(suggestion) },
                        onComplete = { onSuggestionClick(suggestion) }
                    )
                }
            }
            
            if (uiState.recentViewedProducts.isNotEmpty()) {
                item {
                    SectionHeaderWithClearButton(
                        title = stringResource(R.string.recently_viewed),
                        onClearAll = onClearAllViewedProducts
                    )
                }
                
                items(uiState.recentViewedProducts) { product ->
                    RecentViewedProductItem(
                        product = product,
                        onClick = { onProductClick(product.productId) },
                        onDelete = { onDeleteViewedProduct(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LoadingSuggestionsItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.loading_suggestions),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SectionHeaderWithClearButton(
    title: String,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        IconButton(
            onClick = onClearAll,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(R.string.clear_all),
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
