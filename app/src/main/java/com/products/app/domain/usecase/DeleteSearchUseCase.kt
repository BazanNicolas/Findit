package com.products.app.domain.usecase

import com.products.app.domain.model.SearchHistory
import com.products.app.domain.repository.SearchHistoryRepository
import javax.inject.Inject

class DeleteSearchUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(search: SearchHistory) {
        searchHistoryRepository.deleteSearch(search)
    }
}
