package com.example.atry.data.models

data class ProductResponse(
    val data: List<Product>,
    val totalProducts: Int,
    val totalPages: Int,
    val currentPage: Int,
    val perPage: Int
)
