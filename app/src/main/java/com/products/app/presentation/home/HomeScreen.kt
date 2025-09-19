package com.products.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.offset
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.products.app.R
import com.products.app.presentation.common.components.AutosuggestDropdown
import com.products.app.presentation.common.components.ViewedProductCard
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: (String) -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        )
        
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                WelcomeHeader()
                
                Spacer(modifier = Modifier.height(32.dp))
                
                SearchSection(
                    searchQuery = uiState.searchQuery,
                    onQueryChange = viewModel::onQueryChange,
                    onSearchClick = {
                        viewModel.onSearchClick()
                        onSearchClick(uiState.searchQuery)
                    },
                    suggestions = uiState.suggestions,
                    showSuggestions = uiState.showSuggestions,
                    loadingSuggestions = uiState.loadingSuggestions,
                    searchHistory = uiState.searchHistory,
                    showSearchHistory = uiState.showSearchHistory,
                    onSuggestionClick = viewModel::onSuggestionClick,
                    onHistoryClick = viewModel::onHistoryClick,
                    onHideSuggestions = viewModel::hideSuggestions,
                    onShowSearchHistory = viewModel::showSearchHistory
                )
                
                if (uiState.recentSearches.isNotEmpty() || uiState.recentViewedProducts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    ContentSections(
                        recentSearches = uiState.recentSearches,
                        recentViewedProducts = uiState.recentViewedProducts,
                        onSearchClick = onSearchClick,
                        onProductClick = onProductClick
                    )
                }
            }
            
            // Dropdown overlay positioned absolutely
            if (uiState.showSuggestions || uiState.showSearchHistory) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { viewModel.hideSuggestions() } // Close dropdown when clicking outside
                ) {
                    AutosuggestDropdown(
                        suggestions = uiState.suggestions,
                        loadingSuggestions = uiState.loadingSuggestions,
                        searchHistory = uiState.searchHistory,
                        onSuggestionClick = { query -> 
                            val suggestion = uiState.suggestions.find { it.query == query }
                            suggestion?.let { 
                                viewModel.onSuggestionClick(it)
                                viewModel.onSearchClick()
                                onSearchClick(uiState.searchQuery)
                                viewModel.hideSuggestions()
                            }
                        },
                        onHistoryClick = { query ->
                            val history = uiState.searchHistory.find { it.query == query }
                            history?.let { 
                                viewModel.onHistoryClick(it)
                                viewModel.onSearchClick()
                                onSearchClick(uiState.searchQuery)
                                viewModel.hideSuggestions()
                            }
                        },
                        onSuggestionComplete = { query ->
                            val suggestion = uiState.suggestions.find { it.query == query }
                            suggestion?.let { 
                                viewModel.onSuggestionClick(it)
                                viewModel.hideSuggestions()
                            }
                        },
                        onHistoryComplete = { query ->
                            val history = uiState.searchHistory.find { it.query == query }
                            history?.let { 
                                viewModel.onHistoryClick(it)
                                viewModel.hideSuggestions()
                            }
                        },
                        onClearHistory = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .offset(y = 120.dp) // Position closer to search field
                            .clickable { } // Prevent click propagation to background
                    )
                }
            }
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchSection(
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    suggestions: List<com.products.app.domain.model.SearchSuggestion>,
    showSuggestions: Boolean,
    loadingSuggestions: Boolean,
    searchHistory: List<com.products.app.domain.model.SearchHistory>,
    showSearchHistory: Boolean,
    onSuggestionClick: (com.products.app.domain.model.SearchSuggestion) -> Unit,
    onHistoryClick: (com.products.app.domain.model.SearchHistory) -> Unit,
    onHideSuggestions: () -> Unit,
    onShowSearchHistory: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Search container with elevation
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = stringResource(R.string.what_are_you_looking_for),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onQueryChange,
                    placeholder = { Text(stringResource(R.string.search_placeholder)) },
                    trailingIcon = {
                        if (showSuggestions || showSearchHistory) {
                            // Show close button when dropdown is visible
                            IconButton(
                                onClick = onHideSuggestions
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.close),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else if (searchQuery.isNotBlank()) {
                            // Show search button when there's text
                            IconButton(
                                onClick = onSearchClick
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_button),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        imeAction = androidx.compose.ui.text.input.ImeAction.Search
                    ),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                        onSearch = { onSearchClick() }
                    )
                )
            }
        }
    }
}

@Composable
private fun ContentSections(
    recentSearches: List<com.products.app.domain.model.SearchHistory>,
    recentViewedProducts: List<com.products.app.domain.model.ViewedProduct>,
    onSearchClick: (String) -> Unit,
    onProductClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (recentSearches.isNotEmpty()) {
            RecentSearchesSection(
                recentSearches = recentSearches,
                onSearchClick = onSearchClick
            )
        }
        
        if (recentViewedProducts.isNotEmpty()) {
            RecentViewedProductsSection(
                recentViewedProducts = recentViewedProducts,
                onProductClick = onProductClick
            )
        }
    }
}

@Composable
private fun RecentSearchesSection(
    recentSearches: List<com.products.app.domain.model.SearchHistory>,
    onSearchClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_schedule_24),
                    contentDescription = stringResource(R.string.recent_searches),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.recent_searches),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                items(recentSearches) { search ->
                    val chipInteractionSource = remember { MutableInteractionSource() }
                    val isChipPressed by chipInteractionSource.collectIsPressedAsState()
                    
                    val chipScale by animateFloatAsState(
                        targetValue = if (isChipPressed) 0.95f else 1f,
                        animationSpec = tween(150),
                        label = "chipScale"
                    )
                    
                    Surface(
                        modifier = Modifier
                            .clickable(
                                interactionSource = chipInteractionSource,
                                indication = null
                            ) { onSearchClick(search.query) }
                            .scale(chipScale)
                            .padding(4.dp),
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = search.query,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentViewedProductsSection(
    recentViewedProducts: List<com.products.app.domain.model.ViewedProduct>,
    onProductClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = stringResource(R.string.recently_viewed),
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.recently_viewed),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ) {
                items(recentViewedProducts) { product ->
                    ViewedProductCard(
                        modifier = Modifier.width(140.dp),
                        product = product,
                        onProductClick = onProductClick
                    )
                }
            }
        }
    }
}



