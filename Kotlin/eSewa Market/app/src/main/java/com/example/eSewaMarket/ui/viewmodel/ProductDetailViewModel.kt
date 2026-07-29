package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductDetailViewModel: ViewModel() {
    private val productRepository = ProductRepository()

    val selectedProduct: LiveData<Product> = productRepository.selectedProduct

    fun loadProduct(id: Int) {
        viewModelScope.launch {
            productRepository.fetchProductById(id)
        }
    }
}