package com.products.app.data.remote.dto

import com.squareup.moshi.Json

data class BuyBoxWinnerDto(
    @field:Json(name = "item_id") val itemId: String?,
    @field:Json(name = "category_id") val categoryId: String?,
    @field:Json(name = "seller_id") val sellerId: Long?,
    val price: Double?,
    @field:Json(name = "currency_id") val currencyId: String?,
    val shipping: ShippingDto?
)

data class ShippingDto(
    val mode: String?
)
