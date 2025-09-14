package com.products.app.data.mapper

import com.products.app.data.local.entity.SearchHistoryEntity
import com.products.app.domain.model.SearchHistory

fun SearchHistoryEntity.toDomain(): SearchHistory {
    return SearchHistory(
        query = query,
        timestamp = timestamp
    )
}

fun SearchHistory.toEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
        query = query,
        timestamp = timestamp
    )
}
