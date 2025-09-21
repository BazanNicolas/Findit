package com.products.app.data.repository

import com.products.app.core.AppResult
import com.products.app.core.NetworkErrorHandler
import com.products.app.data.mapper.toDomain
import com.products.app.data.remote.AutosuggestApi
import com.products.app.domain.repository.AutosuggestRepository
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

/**
 * Implementation of AutosuggestRepository for handling search suggestions.
 * 
 * This repository provides autocomplete functionality by connecting to MercadoLibre's
 * autosuggest API. It handles query validation, API calls, and error processing to
 * provide real-time search suggestions for improved user experience.
 * 
 * The repository uses a separate Retrofit instance configured specifically for
 * autosuggest operations, allowing for different configurations (like different
 * base URLs or interceptors) compared to the main products API.
 * 
 * @param autosuggestApi The AutosuggestApi interface for autosuggest API calls
 * @param errorHandler The NetworkErrorHandler for processing and localizing errors
 */
class AutosuggestRepositoryImpl @Inject constructor(
    @param:Named("autosuggest") private val autosuggestApi: AutosuggestApi,
    private val errorHandler: NetworkErrorHandler
) : AutosuggestRepository {

    /**
     * Retrieves search suggestions for the given query.
     * 
     * This method provides autocomplete functionality by calling the MercadoLibre
     * autosuggest API. It validates the input query and returns an empty list
     * for empty or whitespace-only queries to avoid unnecessary API calls.
     * 
     * The method handles network errors gracefully and converts API responses
     * to domain models for consistent data handling throughout the application.
     * 
     * @param query The search query to get suggestions for
     * @return AppResult containing either the list of suggestions or an error message
     */
    override suspend fun getSuggestions(query: String): AppResult<List<com.products.app.domain.model.SearchSuggestion>> = try {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isEmpty()) {
            AppResult.Success(emptyList())
        } else {
            val dto = autosuggestApi.getAutosuggest(query = trimmedQuery)
            val suggestions = dto.toDomain()
            AppResult.Success(suggestions)
        }
    } catch (e: Exception) {
        AppResult.Error(errorHandler.handleError(e))
    }
}
