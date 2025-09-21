package com.products.app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Search screen that manages search functionality and history.
 * 
 * This ViewModel coordinates multiple use cases to provide comprehensive search
 * functionality including autocomplete suggestions, search history management,
 * and recently viewed products. It handles real-time search suggestions from
 * the MercadoLibre API and manages local search history.
 * 
 * The ViewModel follows the MVVM pattern and uses StateFlow to expose UI state
 * changes to the Compose UI. It automatically loads initial data and provides
 * reactive updates for search suggestions and history management.
 * 
 * @param getRecentSearchesUseCase Use case for retrieving recent search history
 * @param getMatchingSearchesUseCase Use case for finding matching search history
 * @param getAutosuggestUseCase Use case for fetching API search suggestions
 * @param getRecentViewedProductsUseCase Use case for retrieving recently viewed products
 * @param saveSearchUseCase Use case for saving search queries to history
 * @param deleteSearchUseCase Use case for deleting individual search history entries
 * @param clearAllSearchesUseCase Use case for clearing all search history
 * @param deleteViewedProductUseCase Use case for deleting individual viewed products
 * @param clearAllViewedProductsUseCase Use case for clearing all viewed products
 */
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
