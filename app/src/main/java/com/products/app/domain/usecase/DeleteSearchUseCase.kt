package com.products.app.domain.usecase

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import javax.inject.Inject

/**
 * Use case for deleting a specific search entry from the search history.
 * 
 * This use case handles the business logic for removing a single search
 * entry from the local storage. It's typically used when the user wants
 * to remove a specific search from their search history.
 * 
 * The operation removes only the specified search record from the
 * local database, leaving other search entries intact.
 * 
 * @param searchHistoryRepository The SearchHistoryRepository for data persistence
 */
class DeleteSearchUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    /**
     * Executes the delete search operation.
     * 
     * This method removes the specified search entry from the local
     * database, effectively removing it from the user's search history.
     * 
     * @param search The SearchHistory entry to remove from the database
     */
    suspend operator fun invoke(search: SearchHistory) {
        searchHistoryRepository.deleteSearch(search)
    }
}
