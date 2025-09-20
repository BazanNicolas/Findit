package com.products.app.data.remote

import com.products.app.core.PaginationConstants
import com.products.app.data.remote.dto.ProductDetailDto
import com.products.app.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit interface for MercadoLibre Products API.
 * 
 * This interface defines the contract for accessing MercadoLibre's product-related
 * API endpoints. It provides methods for searching products and retrieving
 * detailed product information.
 * 
 * All endpoints return JSON responses that are automatically deserialized
 * using Moshi converters configured in the Retrofit instance.
 * 
 * @see https://developers.mercadolibre.com/es_ar/api-docs-es
 */
interface ProductsApi {
    
    /**
     * Searches for products using the MercadoLibre API.
     * 
     * This endpoint returns a paginated list of products matching the search criteria.
     * The results include basic product information suitable for listing views.
     * 
     * @param query The search term (required)
     * @param siteId The site ID (default: "MLA" for Argentina)
     * @param status Product status filter (default: "active")
     * @param offset Number of items to skip for pagination (optional)
     * @param limit Maximum number of items to return (default: from PaginationConstants)
     * @return SearchResponseDto containing products and pagination information
     */
    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("site_id") siteId: String = "MLA",
        @Query("status") status: String = "active",
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = PaginationConstants.DEFAULT_PAGE_SIZE
    ): SearchResponseDto
    
    /**
     * Retrieves detailed information about a specific product.
     * 
     * This endpoint returns comprehensive product information including
     * images, attributes, features, and other detailed data.
     * 
     * @param productId The unique identifier of the product
     * @return ProductDetailDto containing detailed product information
     */
    @GET("products/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId: String
    ): ProductDetailDto
}