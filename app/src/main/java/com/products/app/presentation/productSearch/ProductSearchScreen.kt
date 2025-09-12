package com.products.app.presentation.productSearch

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.products.app.presentation.common.components.AppBar
import com.products.app.presentation.common.components.EmptyState
import com.products.app.presentation.common.components.ErrorState
import com.products.app.presentation.common.components.LoadingState
import com.products.app.presentation.productSearch.components.ProductCard

@Composable
fun ProductSearchScreen(
    vm: ProductSearchViewModel = hiltViewModel()
) {
    val state by vm.ui.collectAsState()

    Scaffold(
        topBar = {
            AppBar(
                title = "Productos",
                searchQuery = state.query,
                onSearchQueryChange = vm::onQueryChange,
                onSearchClick = { vm.searchFirstPage() },
                showSearch = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            when {
                state.loading -> {
                    LoadingState(
                        message = "Buscando productos..."
                    )
                }
                state.error != null -> {
                    ErrorState(
                        title = "Error al buscar productos",
                        message = state.error ?: "Error desconocido",
                        onRetry = { vm.searchFirstPage() }
                    )
                }
                state.products.isEmpty() && state.query.isBlank() -> {
                    EmptyState(
                        icon = "🔍",
                        title = "Busca productos",
                        subtitle = "Usa la barra de búsqueda para encontrar productos"
                    )
                }
                state.products.isEmpty() && state.query.isNotBlank() && !state.loading -> {
                    EmptyState(
                        icon = "😔",
                        title = "No se encontraron productos",
                        subtitle = "Intenta con otros términos de búsqueda"
                    )
                }
                else -> {
                    LazyVerticalStaggeredGrid(
                        columns = StaggeredGridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalItemSpacing = 12.dp,
                        contentPadding = PaddingValues(vertical = 16.dp)
                    ) {
                        items(state.products) { product ->
                            ProductCard(
                                product = product,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { /* Handle product click */ }
                            )
                        }
                    }
                }
            }
        }
    }
}