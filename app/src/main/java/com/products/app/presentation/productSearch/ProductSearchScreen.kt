package com.products.app.presentation.productSearch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.products.app.R
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.products.app.presentation.common.components.EmptyState
import com.products.app.presentation.common.components.ErrorState
import com.products.app.presentation.common.components.LoadingState
import com.products.app.presentation.common.components.PaginationLoadingIndicator
import com.products.app.presentation.common.components.PaginationErrorIndicator
import com.products.app.presentation.common.components.ProductCard
import com.products.app.presentation.common.components.AppLogo
import com.products.app.presentation.productSearch.components.InfiniteScrollHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSearchScreen(
    initialQuery: String = "",
    onProductClick: (com.products.app.domain.model.Product) -> Unit = {},
    onBackClick: () -> Unit = {},
    vm: ProductSearchViewModel = hiltViewModel()
) {
    val state by vm.ui.collectAsState()
    
    LaunchedEffect(initialQuery) {
        if (initialQuery.isNotBlank()) {
            vm.onQueryChange(initialQuery)
            vm.searchFirstPage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        AppLogo()
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 0.dp)
        ) {
            when {
                state.loading -> {
                    LoadingState()
                }
                state.error != null -> {
                    ErrorState(
                        title = stringResource(R.string.search_error),
                        message = state.error ?: stringResource(R.string.unknown_error),
                        onRetry = { vm.searchFirstPage() }
                    )
                }
                state.products.isEmpty() && state.query.isBlank() -> {
                    EmptyState(
                        icon = stringResource(R.string.icon_search),
                        title = stringResource(R.string.search_products),
                        subtitle = stringResource(R.string.search_products_subtitle)
                    )
                }
                state.products.isEmpty() && state.query.isNotBlank() && !state.loading -> {
                    EmptyState(
                        icon = stringResource(R.string.icon_sad),
                        title = stringResource(R.string.no_products_found),
                        subtitle = stringResource(R.string.no_products_found_subtitle)
                    )
                }
                else -> {
                    val gridState = remember { LazyStaggeredGridState() }
                    
                    InfiniteScrollHandler(
                        gridState = gridState,
                        onLoadMore = vm::loadNextPage,
                        isLoading = state.loadingMore,
                        hasReachedEnd = state.hasReachedEnd
                    )
                    
                    LazyVerticalStaggeredGrid(
                        state = gridState,
                        columns = StaggeredGridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalItemSpacing = 8.dp,
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
                    ) {
                        items(state.products) { product ->
                            ProductCard(
                                product = product,
                                onProductClick = onProductClick,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        if (state.loadingMore) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                PaginationLoadingIndicator()
                            }
                        }
                        
                        val paginationError = state.paginationError
                        if (paginationError != null) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                PaginationErrorIndicator(
                                    onRetry = vm::retryPagination
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}