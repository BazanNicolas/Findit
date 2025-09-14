package com.products.app.domain.usecase

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMatchingSearchesUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    operator fun invoke(query: String, limit: Int = 10): Flow<List<SearchHistory>> {
        return searchHistoryRepository.getMatchingSearches(query, limit)
    }
}
