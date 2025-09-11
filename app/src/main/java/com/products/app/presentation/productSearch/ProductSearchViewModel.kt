package com.products.app.presentation.productSearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.core.AppResult
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
import com.products.app.presentation.productSearch.state.ProductSearchUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProductSearchViewModel @Inject constructor(
    private val repo: ProductsRepository   // ⬅️ inyectamos el repositorio
) : ViewModel() {

    private val _ui = MutableStateFlow(ProductSearchUiState())
    val ui: StateFlow<ProductSearchUiState> = _ui

    fun onQueryChange(q: String) {
        _ui.update { it.copy(query = q) }
    }

    /** Primera página (offset = 0) */
    fun searchFirstPage(limit: Int = 20) = viewModelScope.launch {
        val q = ui.value.query.trim()
        if (q.isEmpty()) return@launch

        _ui.update { it.copy(loading = true, error = null) }

        when (val res = repo.search(query = q, offset = 0, limit = limit)) {
            is AppResult.Success -> applyResult(firstPage = true, res = res.data)
            is AppResult.Error -> _ui.update { it.copy(loading = false, error = res.message) }
        }
    }

    /** Siguiente página (usa paging actual para calcular el nuevo offset) */
    fun loadNextPage() = viewModelScope.launch {
        val current = ui.value
        val q = current.query.trim()
        val paging = current.paging ?: return@launch

        // Prevent unnecessary API calls when all items are loaded
        val nextOffset = paging.offset + paging.limit
        if (current.products.size >= paging.total) return@launch

        _ui.update { it.copy(loading = true, error = null) }

        when (val res = repo.search(query = q, offset = nextOffset, limit = paging.limit)) {
            is AppResult.Success -> applyResult(firstPage = false, res = res.data)
            is AppResult.Error -> _ui.update { it.copy(loading = false, error = res.message) }
        }
    }

    /** Fusiona resultados en el estado (primera página vs append) */
    private fun applyResult(firstPage: Boolean, res: ProductSearchResult) {
        _ui.update {
            val newList = if (firstPage) res.products else it.products + res.products
            it.copy(
                loading = false,
                products = newList,
                paging = res.paging,
                error = null
            )
        }
    }
}
