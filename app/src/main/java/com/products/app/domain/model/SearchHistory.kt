package com.products.app.domain.model

/**
 * Represents a search query saved in the user's search history.
 * 
 * This data class stores information about previous searches performed by the user,
 * allowing for quick access to recent searches and search suggestions.
 * 
 * @property query The search term that was entered
 * @property timestamp When the search was performed (Unix timestamp)
 */
data class SearchHistory(
    val query: String,
    val timestamp: Long
)
