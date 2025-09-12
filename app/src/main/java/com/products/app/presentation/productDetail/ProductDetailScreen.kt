package com.products.app.presentation.productDetail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.products.app.presentation.common.components.AppBar

@Composable
fun ProductDetailScreen(
    productId: String,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            AppBar(
                title = "Detalle del Producto",
                showSearch = false // No search in detail screen
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Producto ID: $productId",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Text(
                text = "Esta pantalla demuestra cómo el AppBar es reutilizable en diferentes pantallas.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Características del AppBar:",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Reutilizable en cualquier pantalla",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "• Búsqueda integrada opcional",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "• Título personalizable",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "• Diseño consistente",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
