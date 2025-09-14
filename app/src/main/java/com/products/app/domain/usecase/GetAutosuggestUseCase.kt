package com.products.app.domain.usecase

import com.products.app.core.AppResult
import com.products.app.domain.model.SearchSuggestion
import com.products.app.domain.repository.AutosuggestRepository
import javax.inject.Inject

class GetAutosuggestUseCase @Inject constructor(
    private val repository: AutosuggestRepository
) {
    suspend operator fun invoke(query: String): AppResult<List<SearchSuggestion>> {
        return repository.getSuggestions(query.trim())
    }
}
