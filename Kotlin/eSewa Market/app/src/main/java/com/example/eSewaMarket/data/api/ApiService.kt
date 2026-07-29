package com.example.eSewaMarket.data.api

import com.example.eSewaMarket.data.models.HotDeal
import com.example.eSewaMarket.data.models.PageResponse
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.models.ProductResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {

    @GET("categories")
    suspend fun getHotDealCategories(): List<HotDeal>

    @GET("products")
    suspend fun getProduct(): List<Product>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Product

    @GET("products/page")
    suspend fun getRecommendedProducts(
        @Query("page") page: Int,
        @Query("size") size: Int = 8,
        @Query("sortBy") sortBy: String = "id",
        @Query("direction") direction: String = "asc"
    ): PageResponse<ProductResponse>
}