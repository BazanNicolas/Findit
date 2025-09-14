package com.products.app.domain.repository

import com.products.app.core.AppResult
import com.products.app.domain.model.SearchSuggestion

interface AutosuggestRepository {
    suspend fun getSuggestions(query: String): AppResult<List<SearchSuggestion>>
}
