package com.example.atry.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.atry.data.api.RetrofitInstance
import com.example.atry.data.models.Product
import okhttp3.Response

class ProductRepository {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    suspend fun fetchProducts() {
        try {
            val response = RetrofitInstance.productApi.getProduct()
            _products.postValue(response)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", e.toString(), e)
        }
    }

    suspend fun fetchProductById(id: Int){
        try {
            val response = RetrofitInstance.productApi.getProductById(id)
            _selectedProduct.postValue(response)
        } catch (e: Exception){
            android.util.Log.e("API_ERROR", e.toString(), e)
        }
    }
}