package com.example.eSewaMarket.data.models

data class ProductResponse(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val category: ProductCategory,
    val thumbnail: String,
    val images: List<String>? = null,
    val brand: String? = null,
    val discountPercentage: Double? = null
)
