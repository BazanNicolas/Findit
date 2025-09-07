package com.products.app.domain.model

data class BuyBoxInfo(
    val price: Double,
    val currencyId: String,
    val sellerId: Long?
)