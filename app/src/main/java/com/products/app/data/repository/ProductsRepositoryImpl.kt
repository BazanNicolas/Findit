package com.products.app.data.repository

import com.products.app.core.AppResult
import com.products.app.core.NetworkErrorHandler
import com.products.app.data.mapper.toDomain
import com.products.app.data.remote.ProductsApi
import com.products.app.domain.model.Paging
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
import jakarta.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val api: ProductsApi,
    private val errorHandler: NetworkErrorHandler
) : ProductsRepository {

        override suspend fun search(
            query: String,
            offset: Int,
            limit: Int
        ): AppResult<ProductSearchResult> = try {
            val dto = api.searchProducts(
                query = query,
                siteId = "MLA",
                status = "active",
                offset = offset,
                limit = limit
            )
            AppResult.Success(dto.toDomain())
        } catch (e: Exception) {
            AppResult.Error(errorHandler.handleError(e))
        }
}