package com.products.app.data.remote.dto

data class BuyBoxWinnerDto(
    val item_id: String?,
    val category_id: String?,
    val seller_id: Long?,
    val price: Double?,
    val currency_id: String?,
    val shipping: ShippingDto?
)

data class ShippingDto(
    val mode: String?
)
