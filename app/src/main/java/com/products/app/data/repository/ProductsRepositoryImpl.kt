package com.products.app.data.repository

import com.products.app.core.AppResult
import com.products.app.data.mapper.toDomain
import com.products.app.data.remote.ProductsApi
import com.products.app.domain.model.Paging
import com.products.app.domain.model.ProductSearchResult
import com.products.app.domain.repository.ProductsRepository
import jakarta.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val api: ProductsApi
) : ProductsRepository {

    override suspend fun search(
        query: String,
        offset: Int,
        limit: Int
    ): AppResult<ProductSearchResult> = try {
        val dto = api.searchProducts(query = query, offset = offset, limit = limit)
        val products = dto.results.map { it.toDomain() }
        val paging = Paging(
            total = dto.paging.total,
            limit = dto.paging.limit,
            offset = dto.paging.offset
        )
        AppResult.Success(ProductSearchResult(products, paging))
    } catch (e: Exception) {
        AppResult.Error(e.message ?: "Error desconocido")
    }
}