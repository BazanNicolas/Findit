package com.products.app.domain.usecase

import com.products.app.domain.repository.SearchHistoryRepository
import javax.inject.Inject

/**
 * Use case for clearing all search history entries from the local database.
 * 
 * This use case handles the business logic for removing all search history
 * entries from the local storage. It's typically used when the user wants
 * to clear their complete search history.
 * 
 * The operation is irreversible and will remove all search history records
 * from the local database, effectively resetting the user's search history.
 * 
 * @param searchHistoryRepository The SearchHistoryRepository for data persistence
 */
class ClearSearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    /**
     * Executes the clear search history operation.
     * 
     * This method removes all search history entries from the local database,
     * effectively clearing the user's complete search history.
     */
    suspend operator fun invoke() {
        searchHistoryRepository.clearAllSearches()
    }
}
