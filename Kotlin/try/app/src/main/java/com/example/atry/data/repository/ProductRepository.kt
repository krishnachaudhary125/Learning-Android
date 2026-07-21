package com.example.atry.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.atry.data.api.RetrofitInstance
import com.example.atry.data.models.Product

class ProductRepository {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    suspend fun fetchProducts() {
        try {
            val response = RetrofitInstance.productApi.getProduct()
            _products.postValue(response)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", e.toString(), e)
        }
    }
}