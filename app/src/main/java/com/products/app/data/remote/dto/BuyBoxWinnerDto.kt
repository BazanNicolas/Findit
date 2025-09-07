package com.products.app.data.remote.dto
data class BuyBoxWinnerDto(
    val itemId: String?,
    val categoryId: String?,
    val sellerId: Long?,
    val price: Double?,
    val currencyId: String?,
    val shipping: ShippingDto?
)

data class ShippingDto(
    val mode: String?
)
