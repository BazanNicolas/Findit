package com.products.app.presentation.productSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.presentation.productSearch.state.ProductSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductSearchViewModel @Inject constructor() : ViewModel() {

    private val _ui = MutableStateFlow(ProductSearchUiState())
    val ui: StateFlow<ProductSearchUiState> = _ui

    fun simulateLoading() = viewModelScope.launch {
        _ui.update { it.copy(loading = true, message = "Cargando...") }
        delay(1000)
        _ui.update { it.copy(loading = false, message = "Hola mundo") }
    }
}
