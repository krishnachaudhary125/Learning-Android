package com.example.atry.data.models

data class ProductResponse(
    val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val category: ProductCategory,
    val thumbnail: String,
    val images: List<String>? = null,
    val brand: String? = null
)
