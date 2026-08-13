package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.data.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    fun cartCount() = repository.totalQuantity()

    fun addToCart(productId: Long) {
        viewModelScope.launch {
            repository.addToCart(productId)
        }
    }

    fun removeOneFromCart(productId: Long) {
        viewModelScope.launch {
            repository.removeOneFromCart(productId)
        }
    }

    fun productQuantity(productId: Long) =
        repository.productQuantity(productId)

    fun cartProducts() : Flow<List<ProductResponse>>{
        return repository.cartProducts()
    }

    val totalPrice: StateFlow<Double?> = flow {
        emitAll(repository.totalPrice())
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        null
    )
}