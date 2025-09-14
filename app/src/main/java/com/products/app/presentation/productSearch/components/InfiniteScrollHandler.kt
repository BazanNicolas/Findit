package com.products.app.presentation.productSearch.components

import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember

@Composable
fun InfiniteScrollHandler(
    gridState: LazyStaggeredGridState,
    onLoadMore: () -> Unit,
    isLoading: Boolean,
    hasReachedEnd: Boolean
) {
        val shouldLoadMore by remember {
            derivedStateOf {
                val layoutInfo = gridState.layoutInfo
                val visibleItemsInfo = layoutInfo.visibleItemsInfo
                val lastVisibleItem = visibleItemsInfo.lastOrNull()
                
                val threshold = minOf(3, layoutInfo.totalItemsCount / 4)
                val shouldLoad = !isLoading &&
                               !hasReachedEnd &&
                               lastVisibleItem != null &&
                               lastVisibleItem.index >= layoutInfo.totalItemsCount - threshold
                
                shouldLoad
            }
        }
    
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }
}
