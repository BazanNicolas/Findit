package com.products.app.domain.usecase

import com.products.app.domain.repository.SearchHistoryRepository
import javax.inject.Inject

/**
 * Use case for saving search queries to the search history.
 * 
 * This use case handles the business logic for persisting search queries
 * to the local database. It validates the input query and only saves
 * non-blank queries to prevent storing empty or whitespace-only searches.
 * 
 * The use case automatically trims the input query and validates that
 * it's not blank before saving to the repository.
 * 
 * @param searchHistoryRepository The SearchHistoryRepository for data persistence
 */
class SaveSearchUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    /**
     * Executes the save search operation with the given query.
     * 
     * Only saves queries that are not blank after trimming whitespace.
     * This prevents storing empty or meaningless search entries.
     * 
     * @param query The search term to save to history
     */
    suspend operator fun invoke(query: String) {
        val trimmedQuery = query.trim()
        if (trimmedQuery.isNotBlank()) {
            searchHistoryRepository.saveSearch(trimmedQuery)
        }
    }
}
