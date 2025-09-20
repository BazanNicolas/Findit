package com.products.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.domain.model.SearchHistory
import com.products.app.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Home screen that manages recently viewed products and recent searches.
 * 
 * This ViewModel coordinates between multiple use cases to provide a unified state for the home screen.
 * It handles user interactions like deleting search history and viewed products.
 * 
 * The ViewModel follows the MVVM pattern and uses StateFlow to expose UI state changes
 * to the Compose UI. It manages the loading of initial data and handles user interactions
 * by delegating to appropriate use cases.
 * 
 * @param getRecentSearchesUseCase Use case for retrieving recent search history
 * @param getRecentViewedProductsUseCase Use case for retrieving recently viewed products
 * @param deleteSearchUseCase Use case for deleting individual search history entries
 * @param clearAllSearchesUseCase Use case for clearing all search history
 * @param deleteViewedProductUseCase Use case for deleting individual viewed products
 * @param clearAllViewedProductsUseCase Use case for clearing all viewed products
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRecentSearchesUseCase: GetRecentSearchesUseCase,
    private val getRecentViewedProductsUseCase: GetRecentViewedProductsUseCase,
    private val deleteSearchUseCase: DeleteSearchUseCase,
    private val clearAllSearchesUseCase: ClearAllSearchesUseCase,
    private val deleteViewedProductUseCase: DeleteViewedProductUseCase,
    private val clearAllViewedProductsUseCase: ClearAllViewedProductsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
    }
    
    /**
     * Loads initial data for the home screen.
     * 
     * This method combines recent searches and recently viewed products
     * to populate the initial state of the home screen.
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            combine(
                getRecentSearchesUseCase(5),
                getRecentViewedProductsUseCase(6)
            ) { recentSearches, recentViewedProducts ->
                _uiState.value = _uiState.value.copy(
                    recentSearches = recentSearches,
                    recentViewedProducts = recentViewedProducts
                )
            }.collect()
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
