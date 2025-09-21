package com.products.app.domain.repository

import com.products.app.core.AppResult
import com.products.app.domain.model.SearchSuggestion

/**
 * Repository interface for managing autocomplete search suggestions.
 * 
 * This interface defines the contract for autocomplete data access,
 * providing methods for retrieving search suggestions from external APIs.
 * It abstracts the data source details and provides a clean interface
 * for the domain layer to interact with autocomplete functionality.
 * 
 * The repository pattern ensures separation of concerns and makes the
 * codebase more testable and maintainable.
 */
interface AutosuggestRepository {
    /**
     * Retrieves search suggestions for the given query.
     * 
     * @param query The search term entered by the user
     * @return AppResult containing either the search suggestions or an error message
     */
    suspend fun getSuggestions(query: String): AppResult<List<SearchSuggestion>>
}
