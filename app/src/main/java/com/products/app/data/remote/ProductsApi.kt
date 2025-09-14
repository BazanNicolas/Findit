package com.products.app.data.remote

import com.products.app.core.PaginationConstants
import com.products.app.data.remote.dto.ProductDetailDto
import com.products.app.data.remote.dto.SearchResponseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductsApi {
    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String,
        @Query("site_id") siteId: String = "MLA",
        @Query("status") status: String = "active",
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = PaginationConstants.DEFAULT_PAGE_SIZE
    ): SearchResponseDto
    
    @GET("products/{productId}")
    suspend fun getProductDetail(
        @Path("productId") productId: String
    ): ProductDetailDto
}