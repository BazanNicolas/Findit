package com.products.app.domain.model

/**
 * Represents the result of a product search operation.
 * 
 * This data class encapsulates the response from the MercadoLibre search API,
 * including the list of products, pagination information, and search metadata.
 * It's used to display search results and handle pagination for infinite scrolling.
 * 
 * @property products List of products matching the search criteria
 * @property paging Pagination information for loading more results
 * @property keywords Search keywords used in the query
 * @property usedAttributes Attributes that were used to filter the search
 * @property queryType Type of search query performed
 */
data class ProductSearchResult(
    val products: List<Product>,
    val paging: Paging,
    val keywords: String?,
    val usedAttributes: List<ProductAttribute>?,
    val queryType: String?
)