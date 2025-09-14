package com.products.app.domain.model

data class SearchSuggestion(
    val query: String,
    val matchStart: Int,
    val matchEnd: Int,
    val isVerifiedStore: Boolean
)
