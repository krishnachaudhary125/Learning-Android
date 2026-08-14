package com.example.eSewaMarket.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.models.PageResponse
import com.example.eSewaMarket.data.models.Product

class ProductRepository {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products

    private val _featuredProducts = MutableLiveData<List<Product>>()
    val featuredProducts: LiveData<List<Product>> = _featuredProducts

    private val _hotDealProducts = MutableLiveData<List<Product>>()
    val hotDealProducts: LiveData<List<Product>> = _hotDealProducts

    private val _popularBrandProducts = MutableLiveData<List<Product>>()
    val popularBrandProducts: LiveData<List<Product>> = _popularBrandProducts

    private val _selectedProduct = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product> = _selectedProduct

    suspend fun fetchProducts() {
        try {
            val response = RetrofitInstance.api.getProduct()
            _products.postValue(response)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", e.toString(), e)
            throw e
        }
    }

    suspend fun fetchProductById(id: Long){
        try {
            val response = RetrofitInstance.api.getProductById(id)
            _selectedProduct.postValue(response)
        } catch (e: Exception){
            android.util.Log.e("API_ERROR", e.toString(), e)
            throw e
        }
    }

    suspend fun fetchRecommendedProducts(page: Int): PageResponse<Product> {
        return RetrofitInstance.api.getRecommendedProducts(page)
    }

    suspend fun fetchFeaturedProduct(){
        try {
            val response = RetrofitInstance.api.getFeaturedProducts()
            _featuredProducts.postValue(response)
        } catch (e: Exception) {
            android.util.Log.e("API_ERROR", e.toString(), e)
            throw e
        }
    }

    suspend fun fetchHotDealProducts(){
        try {
            val response = RetrofitInstance.api.getHotDealProducts()
            _hotDealProducts.postValue(response)
        }catch (e: Exception){
            android.util.Log.e("API_ERROR", e.toString(), e)
            throw e
        }
    }

    suspend fun fetchPopularBrandProducts(){
        try {
            val response = RetrofitInstance.api.getPopularBrandProducts()
            _popularBrandProducts.postValue(response)
        }catch (e: Exception){
            android.util.Log.e("API_ERROR", e.toString(), e)
            throw e
        }
    }
}