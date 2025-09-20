package com.products.app.domain.model

/**
 * Represents a search suggestion from the autocomplete API.
 * 
 * This data class contains information about search suggestions that appear
 * as the user types in the search bar, helping them find products more easily.
 * 
 * @property query The suggested search term
 * @property matchStart Start position of the matching text in the suggestion
 * @property matchEnd End position of the matching text in the suggestion
 * @property isVerifiedStore Whether the suggestion is from a verified store
 */
data class SearchSuggestion(
    val query: String,
    val matchStart: Int,
    val matchEnd: Int,
    val isVerifiedStore: Boolean
)
