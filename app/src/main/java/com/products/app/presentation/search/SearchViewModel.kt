package com.products.app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val getMatchingSearchesUseCase: GetMatchingSearchesUseCase,
    private val getAutosuggestUseCase: GetAutosuggestUseCase,
    private val getRecentViewedProductsUseCase: GetRecentViewedProductsUseCase,
    private val saveSearchUseCase: SaveSearchUseCase,
    private val deleteSearchUseCase: DeleteSearchUseCase,
    private val clearAllSearchesUseCase: ClearAllSearchesUseCase,
    private val deleteViewedProductUseCase: DeleteViewedProductUseCase,
    private val clearAllViewedProductsUseCase: ClearAllViewedProductsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            combine(
                getRecentSearchesUseCase(10),
                getRecentViewedProductsUseCase(10)
            ) { recentSearches, recentViewedProducts ->
                _uiState.value = _uiState.value.copy(
                    searchHistory = recentSearches,
                    recentViewedProducts = recentViewedProducts,
                    showSearchHistory = recentSearches.isNotEmpty()
                )
            }.collect()
        }
    }
    
    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        
        if (query.isBlank()) {
            // Load history only when no text is entered
            loadRecentHistory()
        } else {
            // Load suggestions and history only when text is entered
            loadMatchingHistory(query)
            loadSuggestions(query)
        }
    }
    
    private fun loadRecentHistory() {
        viewModelScope.launch {
            getRecentSearchesUseCase(10)
                .onEach { history ->
                    _uiState.value = _uiState.value.copy(
                        searchHistory = history,
                        showSearchHistory = history.isNotEmpty(),
                        showSuggestions = false
                    )
                }
                .collect()
        }
    }
    
    private fun loadMatchingHistory(query: String) {
        viewModelScope.launch {
            getMatchingSearchesUseCase(query)
                .onEach { history ->
                    _uiState.value = _uiState.value.copy(
                        searchHistory = history,
                        showSearchHistory = history.isNotEmpty(),
                        showSuggestions = false
                    )
                }
                .collect()
        }
    }
    
    private fun loadSuggestions(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loadingSuggestions = true)
            
            val result = getAutosuggestUseCase(query)
            when (result) {
                is com.products.app.core.AppResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        suggestions = result.data,
                        showSuggestions = result.data.isNotEmpty(),
                        loadingSuggestions = false
                    )
                }
                is com.products.app.core.AppResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        loadingSuggestions = false,
                        showSuggestions = false,
                        error = result.message
                    )
                }
            }
        }
    }
    
    fun onSuggestionClick(suggestion: com.products.app.domain.model.SearchSuggestion) {
        _uiState.value = _uiState.value.copy(
            searchQuery = suggestion.query,
            showSuggestions = false
        )
    }
    
    fun onHistoryClick(history: SearchHistory) {
        _uiState.value = _uiState.value.copy(
            searchQuery = history.query,
            showSearchHistory = false
        )
    }
    
    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            showSuggestions = false,
            showSearchHistory = false
        )
        loadRecentHistory()
    }
    
    fun onSearchClick() {
        val query = _uiState.value.searchQuery.trim()
        if (query.isNotBlank()) {
            viewModelScope.launch {
                saveSearchUseCase(query)
            }
        }
    }
    
    fun deleteSearch(search: SearchHistory) {
        viewModelScope.launch {
            deleteSearchUseCase(search)
        }
    }
    
    fun clearAllSearches() {
        viewModelScope.launch {
            clearAllSearchesUseCase()
        }
    }
    
    fun deleteViewedProduct(viewedProduct: com.products.app.domain.model.ViewedProduct) {
        viewModelScope.launch {
            deleteViewedProductUseCase(viewedProduct)
        }
    }
    
    fun clearAllViewedProducts() {
        viewModelScope.launch {
            clearAllViewedProductsUseCase()
        }
    }
}
