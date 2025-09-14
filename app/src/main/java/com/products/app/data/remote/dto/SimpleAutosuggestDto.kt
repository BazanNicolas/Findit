package com.products.app.data.remote.dto

data class SimpleAutosuggestDto(
    val q: String?,
    val suggested_queries: List<SimpleSuggestedQueryDto>?,
    val filter_logos: List<String>?
)

data class SimpleSuggestedQueryDto(
    val q: String?,
    val match_start: Int?,
    val match_end: Int?,
    val is_verified_store: Boolean?,
    val filters: List<String>?
)
