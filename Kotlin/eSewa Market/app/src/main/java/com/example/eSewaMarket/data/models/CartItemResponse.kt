package com.example.eSewaMarket.data.models

data class CartItemResponse(
    val productId: Long,
    val title: String,
    val thumbnail: String,
    val price: Double,
    val quantity: Int
)