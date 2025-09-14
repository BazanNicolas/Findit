package com.products.app.domain.usecase

import com.products.app.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class SaveSearchUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(query: String) {
        if (query.isNotBlank()) {
            searchHistoryRepository.saveSearch(query)
        }
    }
}
