package com.example.eSewaMarket.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.models.PageResponse
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.models.ProductResponse

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

    suspend fun fetchRecommendedProducts(page: Int, size: Int): PageResponse<ProductResponse> {
        return RetrofitInstance.productApi.getRecommendedProducts(page, size)
    }
}