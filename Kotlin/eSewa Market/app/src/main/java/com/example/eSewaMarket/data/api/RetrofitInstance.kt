package com.example.eSewaMarket.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitInstance {

    private const val BASE_URL = "https://casualty-elevator-yodel.ngrok-free.dev/"

    val productApi: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}