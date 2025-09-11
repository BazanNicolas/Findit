package com.products.app.presentation.productSearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.products.app.presentation.productSearch.ProductSearchViewModel

@Composable
fun ProductSearchScreen(
    vm: ProductSearchViewModel = hiltViewModel()
) {
    val state by vm.ui.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo de búsqueda
        OutlinedTextField(
            value = state.query,
            onValueChange = vm::onQueryChange,
            label = { Text("Buscar productos") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // Botón de buscar
        Button(
            onClick = { vm.searchFirstPage() },
            enabled = !state.loading && state.query.isNotBlank()
        ) {
            Text(if (state.loading) "Buscando..." else "Buscar")
        }

        Spacer(Modifier.height(16.dp))

        // Contenido según estado
        when {
            state.loading -> {
                CircularProgressIndicator()
            }
            state.error != null -> {
                Text("Error: ${state.error}", color = MaterialTheme.colorScheme.error)
            }
            state.products.isEmpty() -> {
                Text("No hay resultados todavía")
            }
            else -> {
                LazyColumn {
                    items(state.products, key = { it.id }) { product ->
                        ListItem(
                            headlineContent = { Text(product.name) },
                            supportingContent = {
                                Text(product.thumbnailUrl ?: "Sin imagen")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // TODO: Navigate to product detail
                                }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}
