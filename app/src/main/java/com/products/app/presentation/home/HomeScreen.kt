package com.products.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.filled.Delete
import com.products.app.R
import com.products.app.presentation.common.components.ViewedProductCard
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

/**
 * The main home screen of the Products application.
 * 
 * This screen serves as the landing page and provides access to:
 * - Recent search history
 * - Recently viewed products
 * - Quick navigation to search screen
 * 
 * The screen follows Material Design 3 principles and provides a clean,
 * intuitive interface for users to discover and search for products.
 * 
 * @param onSearchClick Callback invoked when a search is performed
 * @param onProductClick Callback invoked when a product is selected
 * @param onNavigateToSearch Callback invoked when navigating to the search screen
 * @param viewModel The HomeViewModel instance (injected via Hilt)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        )
        
        // Decorative elements
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                            androidx.compose.ui.graphics.Color.Transparent
                        ),
                        radius = 400f
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
                    onNavigateToSearch = onNavigateToSearch
                )
                
                if (uiState.recentSearches.isNotEmpty() || uiState.recentViewedProducts.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    ContentSections(
                        recentSearches = uiState.recentSearches,
                        recentViewedProducts = uiState.recentViewedProducts,
                        onSearchClick = onSearchClick,
                        onProductClick = onProductClick,
                        onDeleteSearch = { search -> viewModel.deleteSearch(search) },
                        onClearAllSearches = { viewModel.clearAllSearches() },
                        onDeleteViewedProduct = { product -> viewModel.deleteViewedProduct(product) },
                        onClearAllViewedProducts = { viewModel.clearAllViewedProducts() }
                    )
                } else {
                    Spacer(modifier = Modifier.height(48.dp))
                    
                    EmptyStateDecoration()
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
            text = buildAnnotatedString {
                val welcomeText = stringResource(R.string.welcome_title)
                val findItIndex = welcomeText.indexOf("Findit")
                
                if (findItIndex != -1) {
                    append(welcomeText.substring(0, findItIndex))
                    
                    withStyle(
                        style = androidx.compose.ui.text.SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Findit")
                    }
                    
                    append(welcomeText.substring(findItIndex + 6))
                } else {
                    append(welcomeText)
                }
            },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SearchSection(
    onNavigateToSearch: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
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
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToSearch() }
                ) {
                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        placeholder = { 
                            Text(
                                text = stringResource(R.string.search_placeholder),
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            ) 
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = onNavigateToSearch
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search_button),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        ),
                        readOnly = true,
                        enabled = false
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentSections(
    recentSearches: List<com.products.app.domain.model.SearchHistory>,
    recentViewedProducts: List<com.products.app.domain.model.ViewedProduct>,
    onSearchClick: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onDeleteSearch: (com.products.app.domain.model.SearchHistory) -> Unit,
    onClearAllSearches: () -> Unit,
    onDeleteViewedProduct: (com.products.app.domain.model.ViewedProduct) -> Unit,
    onClearAllViewedProducts: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (recentSearches.isNotEmpty()) {
            RecentSearchesSection(
                recentSearches = recentSearches,
                onSearchClick = onSearchClick,
                onDeleteSearch = onDeleteSearch,
                onClearAllSearches = onClearAllSearches
            )
        }
        
        if (recentViewedProducts.isNotEmpty()) {
            RecentViewedProductsSection(
                recentViewedProducts = recentViewedProducts,
                onProductClick = onProductClick,
                onDeleteViewedProduct = onDeleteViewedProduct,
                onClearAllViewedProducts = onClearAllViewedProducts
            )
        }
    }
}

@Composable
private fun RecentSearchesSection(
    recentSearches: List<com.products.app.domain.model.SearchHistory>,
    onSearchClick: (String) -> Unit,
    onDeleteSearch: (com.products.app.domain.model.SearchHistory) -> Unit,
    onClearAllSearches: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                
                IconButton(
                    onClick = onClearAllSearches,
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
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                val listState = rememberLazyListState()
                val canScrollLeft = listState.canScrollBackward
                val canScrollRight = listState.canScrollForward
                
                LazyRow(
                    state = listState,
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
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shadowElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = search.query,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                IconButton(
                                    onClick = { onDeleteSearch(search) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = stringResource(R.string.delete),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                // Left fade gradient - only show when can scroll left
                if (canScrollLeft) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .width(16.dp)
                            .height(56.dp) // Height of chips with padding
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surface,
                                        androidx.compose.ui.graphics.Color.Transparent
                                    )
                                )
                            )
                    )
                }
                
                // Right fade gradient - only show when can scroll right
                if (canScrollRight) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .width(16.dp)
                            .height(56.dp) // Height of chips with padding
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(
                                        androidx.compose.ui.graphics.Color.Transparent,
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentViewedProductsSection(
    recentViewedProducts: List<com.products.app.domain.model.ViewedProduct>,
    onProductClick: (String) -> Unit,
    onDeleteViewedProduct: (com.products.app.domain.model.ViewedProduct) -> Unit,
    onClearAllViewedProducts: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
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
                
                IconButton(
                    onClick = onClearAllViewedProducts,
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
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                val listState = rememberLazyListState()
                val canScrollLeft = listState.canScrollBackward
                val canScrollRight = listState.canScrollForward
                
                LazyRow(
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    items(recentViewedProducts) { product ->
                        ViewedProductCard(
                            modifier = Modifier.width(140.dp),
                            product = product,
                            onProductClick = onProductClick,
                            onDeleteClick = onDeleteViewedProduct
                        )
                    }
                }
                
                // Left fade gradient - only show when can scroll left
                if (canScrollLeft) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .width(16.dp)
                            .height(164.dp) // Height of product cards (120dp image + 44dp text/padding)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surface,
                                        androidx.compose.ui.graphics.Color.Transparent
                                    )
                                )
                            )
                    )
                }
                
                // Right fade gradient - only show when can scroll right
                if (canScrollRight) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .width(16.dp)
                            .height(164.dp) // Height of product cards (120dp image + 44dp text/padding)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                    colors = listOf(
                                        androidx.compose.ui.graphics.Color.Transparent,
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyStateDecoration() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Decorative circles
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                        shape = CircleShape
                    )
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                        shape = CircleShape
                    )
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "¡Comienza a explorar!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            textAlign = TextAlign.Center
        )
    }
}



