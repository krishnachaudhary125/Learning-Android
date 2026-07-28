package com.example.atry.data.models

data class ProductResponse(
    val id: Long,
    val title: String,
    val description: String? = null,
    val price: Double,
    val category: String? = null,
    val thumbnail: String? = null,
    val images: List<String>? = null,
    val brand: String? = null
)
