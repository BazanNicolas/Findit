package com.products.app.domain.model

/**
 * Represents pagination information for search results.
 * 
 * This data class contains metadata about pagination for API responses,
 * allowing the UI to implement infinite scrolling and show pagination controls.
 * 
 * @property total Total number of items available
 * @property limit Maximum number of items per page
 * @property offset Number of items skipped from the beginning
 */
data class Paging(
    val total: Int,
    val limit: Int,
    val offset: Int
)
