package com.products.app.domain.model

/**
 * Represents buy box information for a product.
 * 
 * The buy box is the section on a product page where customers can add items
 * to their cart. This data class contains information about the best offer
 * available for a product, including price and seller details.
 * 
 * @property price The price of the product
 * @property currencyId Currency identifier (e.g., "ARS" for Argentine Peso)
 * @property sellerId Unique identifier of the seller offering this price
 */
data class BuyBoxInfo(
    val price: Double,
    val currencyId: String,
    val sellerId: Long?
)