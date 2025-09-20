package com.products.app.data.mapper

import com.products.app.data.local.entity.SearchHistoryEntity
import com.products.app.domain.model.SearchHistory

/**
 * Extension function to convert SearchHistoryEntity to domain SearchHistory model.
 * 
 * This mapper transforms the database entity to the domain model for use
 * in the presentation layer.
 * 
 * @return SearchHistory domain model with mapped data
 */
fun SearchHistoryEntity.toDomain(): SearchHistory {
    return SearchHistory(
        query = query,
        timestamp = timestamp
    )
}

/**
 * Extension function to convert domain SearchHistory model to SearchHistoryEntity.
 * 
 * This mapper transforms the domain model to the database entity for storage
 * in the local database.
 * 
 * @return SearchHistoryEntity for database storage
 */
fun SearchHistory.toEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
        query = query,
        timestamp = timestamp
    )
}
