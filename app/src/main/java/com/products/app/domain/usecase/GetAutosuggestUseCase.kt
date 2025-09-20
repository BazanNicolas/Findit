package com.products.app.domain.usecase

import com.products.app.core.AppResult
import com.products.app.domain.model.SearchSuggestion
import com.products.app.domain.repository.AutosuggestRepository
import javax.inject.Inject

/**
 * Use case for retrieving search suggestions from the autocomplete API.
 * 
 * This use case handles the business logic for fetching search suggestions
 * as the user types in the search bar. It delegates to the AutosuggestRepository
 * to fetch suggestions from the MercadoLibre autocomplete API.
 * 
 * The use case automatically trims the input query to remove leading and
 * trailing whitespace before making the API call.
 * 
 * @param repository The AutosuggestRepository for fetching suggestions
 */
class GetAutosuggestUseCase @Inject constructor(
    private val repository: AutosuggestRepository
) {
    /**
     * Executes the autosuggest operation with the given query.
     * 
     * @param query The search term entered by the user
     * @return AppResult containing either the search suggestions or an error message
     */
    suspend operator fun invoke(query: String): AppResult<List<SearchSuggestion>> {
        return repository.getSuggestions(query.trim())
    }
}
