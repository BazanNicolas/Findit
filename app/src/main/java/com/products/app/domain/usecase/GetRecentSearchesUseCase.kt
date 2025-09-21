package com.products.app.domain.usecase

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case for retrieving recent search history entries from the local database.
 * 
 * This use case handles the business logic for fetching recent search queries
 * in chronological order (most recent first). It returns a Flow to provide
 * reactive updates when the search history changes.
 * 
 * The use case supports pagination through the limit parameter, allowing
 * the presentation layer to control how many items to load at once.
 * 
 * @param searchHistoryRepository The SearchHistoryRepository for data access
 */
class GetRecentSearchesUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    /**
     * Executes the get recent searches operation.
     * 
     * This method retrieves the most recent search queries from the
     * local database, ordered by timestamp (most recent first).
     * 
     * @param limit The maximum number of recent searches to return (default: 10)
     * @return Flow containing the list of recent search history entries
     */
    operator fun invoke(limit: Int = 10): Flow<List<SearchHistory>> {
        return searchHistoryRepository.getRecentSearches(limit)
    }
}
