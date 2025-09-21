package com.products.app.domain.usecase

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving search history entries that match a given query.
 * 
 * This use case handles the business logic for finding search history entries
 * that contain or match the provided query string. It's typically used for
 * autocomplete functionality, showing users their previous searches that
 * match what they're currently typing.
 * 
 * The use case returns a Flow to provide reactive updates when the search
 * history changes, and supports pagination through the limit parameter.
 * 
 * @param searchHistoryRepository The SearchHistoryRepository for data access
 */
class GetMatchingSearchesUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    /**
     * Executes the get matching searches operation.
     * 
     * This method retrieves search history entries that match the provided
     * query string, typically used for autocomplete suggestions.
     * 
     * @param query The search term to match against existing search history
     * @param limit The maximum number of matching searches to return (default: 10)
     * @return Flow containing the list of matching search history entries
     */
    operator fun invoke(query: String, limit: Int = 10): Flow<List<SearchHistory>> {
        return searchHistoryRepository.getMatchingSearches(query, limit)
    }
}
