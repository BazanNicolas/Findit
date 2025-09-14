package com.products.app.data.mapper

import com.products.app.data.remote.dto.SimpleAutosuggestDto
import com.products.app.data.remote.dto.SimpleSuggestedQueryDto
import com.products.app.domain.model.SearchSuggestion

fun SimpleAutosuggestDto.toDomain(): List<SearchSuggestion> {
    return suggested_queries?.mapNotNull { it.toDomain() } ?: emptyList()
}

fun SimpleSuggestedQueryDto.toDomain(): SearchSuggestion? {
    val query = q ?: return null
    return SearchSuggestion(
        query = query,
        matchStart = match_start ?: 0,
        matchEnd = match_end ?: query.length,
        isVerifiedStore = is_verified_store ?: false
    )
}
