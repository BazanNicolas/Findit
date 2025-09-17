package com.products.app.util

import com.products.app.domain.model.*
import com.products.app.data.remote.dto.*

/**
 * Factory para crear objetos mock para testing.
 * Proporciona datos consistentes y realistas para tests.
 */
object MockDataFactory {
    
    // Domain Models
    fun createProduct(
        id: String = "MLA123456789",
        name: String = "iPhone 15 Pro Max 256GB",
        status: ProductStatus = ProductStatus.ACTIVE,
        thumbnailUrl: String? = "https://http2.mlstatic.com/D_NQ_NP_123456-MLA70381330133_072023-I.webp"
    ) = Product(
        id = id,
        name = name,
        status = status,
        domainId = "MLA-CELLPHONES",
        permalink = "https://www.mercadolibre.com.ar/apple-iphone-15-pro-max-256-gb/p/$id",
        thumbnailUrl = thumbnailUrl,
        pictureUrls = listOf(
            "https://http2.mlstatic.com/D_NQ_NP_123456-MLA70381330133_072023-I.webp",
            "https://http2.mlstatic.com/D_NQ_NP_789012-MLA70381330133_072023-I.webp"
        ),
        attributes = listOf(
            ProductAttribute("BRAND", "Marca", "15996", "Apple"),
            ProductAttribute("MODEL", "Modelo", "12345", "iPhone 15 Pro Max"),
            ProductAttribute("STORAGE", "Almacenamiento", "67890", "256 GB")
        ),
        shortDescription = "El iPhone más avanzado con chip A17 Pro",
        hasVariants = true,
        buyBox = BuyBoxInfo(
            price = 1299999.0,
            currencyId = "ARS",
            sellerId = 123456L
        ),
        catalogProductId = "MLA123456789",
        qualityType = "premium",
        type = "product",
        keywords = "iphone, apple, smartphone",
        siteId = "MLA"
    )
    
    fun createProductList(count: Int = 5): List<Product> {
        return (1..count).map { index ->
            createProduct(
                id = "MLA12345678$index",
                name = "Producto Test $index"
            )
        }
    }
    
    fun createProductSearchResult(
        products: List<Product> = createProductList(),
        totalResults: Int = products.size,
        offset: Int = 0,
        limit: Int = 10
    ) = ProductSearchResult(
        products = products,
        paging = Paging(
            total = totalResults,
            offset = offset,
            limit = limit
        ),
        keywords = "search keywords",
        usedAttributes = emptyList(),
        queryType = "search"
    )
    
    fun createProductDetail(
        id: String = "MLA123456789",
        name: String = "iPhone 15 Pro Max 256GB"
    ) = ProductDetail(
        id = id,
        catalogProductId = id,
        status = "active",
        pdpTypes = listOf("regular"),
        domainId = "MLA-CELLPHONES",
        permalink = "https://www.mercadolibre.com.ar/apple-iphone-15-pro-max-256-gb/p/$id",
        name = name,
        familyName = null,
        type = "product",
        buyBoxWinner = null,
        pickers = emptyList(),
        pictures = listOf(
            ProductPicture(
                id = "pic1",
                url = "https://http2.mlstatic.com/D_NQ_NP_123456-MLA70381330133_072023-I.webp",
                suggestedForPicker = emptyList(),
                maxWidth = 1200,
                maxHeight = 1200,
                sourceMetadata = null,
                tags = emptyList()
            )
        ),
        descriptionPictures = emptyList(),
        mainFeatures = emptyList(),
        disclaimers = emptyList(),
        attributes = listOf(
            ProductDetailAttribute(
                id = "BRAND",
                name = "Marca",
                valueId = "15996",
                valueName = "Apple",
                values = emptyList(),
                meta = emptyMap()
            )
        ),
        shortDescription = ProductDescription(
            type = "plain_text",
            content = "El iPhone más avanzado con chip A17 Pro"
        ),
        parentId = null,
        userProduct = null,
        childrenIds = emptyList(),
        settings = null,
        qualityType = "premium",
        releaseInfo = null,
        presaleInfo = null,
        enhancedContent = null,
        tags = emptyList(),
        dateCreated = null,
        authorizedStores = null,
        lastUpdated = null,
        grouperId = null,
        experiments = emptyMap()
    )
    
    fun createSearchHistory(
        query: String = "iphone",
        timestamp: Long = System.currentTimeMillis()
    ) = SearchHistory(
        query = query,
        timestamp = timestamp
    )
    
    fun createSearchHistoryList(count: Int = 3): List<SearchHistory> {
        return (1..count).map { index ->
            createSearchHistory(
                query = "búsqueda $index",
                timestamp = System.currentTimeMillis() - (index * 1000L)
            )
        }
    }
    
    fun createViewedProduct(
        productId: String = "MLA123456789",
        productName: String = "iPhone 15 Pro Max"
    ) = ViewedProduct(
        productId = productId,
        productName = productName,
        thumbnailUrl = "https://http2.mlstatic.com/D_NQ_NP_123456-MLA70381330133_072023-I.webp",
        timestamp = System.currentTimeMillis()
    )
    
    fun createViewedProductList(count: Int = 4): List<ViewedProduct> {
        return (1..count).map { index ->
            createViewedProduct(
                productId = "MLA12345678$index",
                productName = "Producto Visto $index"
            )
        }
    }
    
    fun createSearchSuggestion(
        query: String = "iphone 15"
    ) = SearchSuggestion(
        query = query,
        matchStart = 0,
        matchEnd = query.length,
        isVerifiedStore = false
    )
    
    fun createSearchSuggestionList(count: Int = 3): List<SearchSuggestion> {
        return (1..count).map { index ->
            createSearchSuggestion("sugerencia $index")
        }
    }
    
    // DTOs for API testing
    fun createProductDto(
        id: String = "MLA123456789",
        name: String = "iPhone 15 Pro Max 256GB"
    ) = ProductDto(
        id = id,
        date_created = "2024-01-01T00:00:00.000Z",
        catalog_product_id = id,
        pdp_types = listOf("regular"),
        status = "active",
        domain_id = "MLA-CELLPHONES",
        settings = SettingsDto(
            listingStrategy = "premium",
            exclusive = false
        ),
        name = name,
        main_features = listOf(
            MainFeatureDto(
                id = "BRAND",
                value = "Apple"
            )
        ),
        attributes = listOf(
            AttributeDto(
                id = "BRAND",
                name = "Marca",
                value_id = "15996",
                value_name = "Apple",
                values = listOf(
                    AttributeValueDto(
                        id = "15996",
                        name = "Apple",
                        meta = emptyMap()
                    )
                ),
                meta = emptyMap()
            )
        ),
        pictures = listOf(
            PictureDto(
                id = "pic1",
                url = "https://http2.mlstatic.com/D_NQ_NP_123456-MLA70381330133_072023-I.webp"
            )
        ),
        parent_id = null,
        children_ids = emptyList(),
        quality_type = "premium",
        priority = "high",
        last_updated = "2024-01-01T00:00:00.000Z",
        type = "product",
        keywords = "iphone, apple, smartphone",
        site_id = "MLA",
        variations = emptyList(),
        buying_mode = "buy_it_now",
        channels = listOf("marketplace"),
        description = "El iPhone más avanzado con chip A17 Pro",
        permalink = "https://www.mercadolibre.com.ar/apple-iphone-15-pro-max-256-gb/p/$id",
        short_description = ShortDescriptionDto(
            type = "plain_text",
            content = "El iPhone más avanzado con chip A17 Pro"
        ),
        buy_box_winner = BuyBoxWinnerDto(
            item_id = id,
            category_id = "MLA1055",
            seller_id = 123456L,
            price = 1299999.0,
            currency_id = "ARS",
            shipping = ShippingDto(mode = "me2")
        )
    )
    
    fun createSearchResponseDto(
        results: List<ProductDto> = listOf(createProductDto()),
        paging: PagingDto = createPagingDto()
    ) = SearchResponseDto(
        keywords = "iphone",
        paging = paging,
        results = results,
        used_attributes = emptyList(),
        query_type = "search"
    )
    
    fun createPagingDto(
        total: Int = 1000,
        offset: Int = 0,
        limit: Int = 10
    ) = PagingDto(
        total = total,
        offset = offset,
        limit = limit
    )
    
    fun createSimpleAutosuggestDto(
        q: String = "iphone"
    ) = SimpleAutosuggestDto(
        q = q,
        suggested_queries = listOf(
            SimpleSuggestedQueryDto(
                q = "$q pro",
                match_start = 0,
                match_end = q.length,
                is_verified_store = false,
                filters = emptyList()
            )
        ),
        filter_logos = emptyList()
    )
}

// Extension para crear listas fácilmente
fun <T> T.asList(): List<T> = listOf(this)
