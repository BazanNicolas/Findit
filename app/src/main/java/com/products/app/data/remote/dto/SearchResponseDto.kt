package com.products.app.data.remote.dto

import com.squareup.moshi.Json

data class SearchResponseDto(
    val keywords: String?,
    val paging: PagingDto,
    val results: List<ProductDto>
)

data class PagingDto(
    val total: Int,
    val limit: Int,
    val offset: Int
)