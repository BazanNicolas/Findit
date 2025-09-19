package com.products.app.data.remote.dto

data class SearchResponseDto(
    val keywords: String?,
    val paging: PagingDto,
    val results: List<ProductDto>,
    val used_attributes: List<AttributeDto>?,
    val query_type: String?
)

data class PagingDto(
    val total: Int,
    val limit: Int,
    val offset: Int
)