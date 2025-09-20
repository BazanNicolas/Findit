package com.products.app.domain.usecase

import com.products.app.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class ClearAllSearchesUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke() {
        searchHistoryRepository.clearAllSearches()
    }
}
