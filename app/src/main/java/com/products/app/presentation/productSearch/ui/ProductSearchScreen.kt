package com.products.app.presentation.productSearch.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.products.app.presentation.productSearch.ProductSearchViewModel

@Composable
fun ProductSearchScreen(
    vm: ProductSearchViewModel = hiltViewModel()
) {
    val state by vm.ui.collectAsState()

    Box(Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = state.message, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(16.dp))
            Button(onClick = vm::simulateLoading, enabled = !state.loading) {
                Text(if (state.loading) "..." else "Probar")
            }
        }
    }
}