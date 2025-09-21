package com.products.app.presentation.productDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.products.app.core.AppResult
import com.products.app.domain.usecase.GetProductDetailUseCase
import com.products.app.domain.usecase.SaveViewedProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Product Detail screen that manages product information display.
 * 
 * This ViewModel handles loading detailed product information and automatically
 * saves viewed products to the user's viewing history. It coordinates between
 * the product detail use case and the viewed product tracking use case.
 * 
 * The ViewModel follows the MVVM pattern and uses StateFlow to expose UI state
 * changes to the Compose UI. It manages loading states, error handling, and
 * automatic tracking of viewed products for history management.
 * 
 * @param getProductDetailUseCase Use case for retrieving detailed product information
 * @param saveViewedProductUseCase Use case for saving viewed products to history
 */
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailUseCase: GetProductDetailUseCase,
    private val saveViewedProductUseCase: SaveViewedProductUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()
    
    fun loadProductDetail(productId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            when (val result = getProductDetailUseCase(productId)) {
                is AppResult.Success -> {
                    val productDetail = result.data
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        productDetail = productDetail,
                        error = null
                    )
                    
                    // Save viewed product
                    val product = com.products.app.domain.model.Product(
                        id = productDetail.id,
                        name = productDetail.name,
                        status = com.products.app.domain.model.ProductStatus.ACTIVE,
                        domainId = productDetail.domainId,
                        permalink = productDetail.permalink,
                        thumbnailUrl = productDetail.pictures?.firstOrNull()?.url,
                        pictureUrls = productDetail.pictures?.map { it.url } ?: emptyList(),
                        attributes = emptyList(),
                        shortDescription = productDetail.shortDescription?.content,
                        hasVariants = false,
                        buyBox = null,
                        catalogProductId = productDetail.catalogProductId,
                        qualityType = productDetail.qualityType,
                        type = productDetail.type,
                        keywords = null,
                        siteId = null
                    )
                    saveViewedProductUseCase(product)
                }
                is AppResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        productDetail = null,
                        error = result.message
                    )
                }
            }
        }
    }
}
