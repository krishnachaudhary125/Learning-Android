package com.example.atry.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.atry.data.api.RetrofitInstance
import com.example.atry.data.models.Product

class ProductRepository {

    private val _product = MutableLiveData<List<Product>>()

    val product: LiveData<List<Product>> = _product

    suspend fun fetchProduct() {
        try {
            val response = RetrofitInstance.productApi.getProduct()
            _product.postValue(response)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", e.toString(), e)
        }
    }
}