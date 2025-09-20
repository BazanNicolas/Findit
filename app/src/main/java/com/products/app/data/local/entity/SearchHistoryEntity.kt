package com.products.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a search history entry in the local database.
 * 
 * This entity stores search queries performed by the user, allowing the app
 * to provide search history functionality and autocomplete suggestions.
 * The query serves as the primary key to prevent duplicate entries.
 * 
 * @property query The search term that was entered (Primary Key)
 * @property timestamp When the search was performed (defaults to current time)
 */
@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey
    val query: String,
    val timestamp: Long = System.currentTimeMillis()
)
