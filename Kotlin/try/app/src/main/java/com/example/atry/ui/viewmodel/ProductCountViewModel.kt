package com.example.atry.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atry.data.repository.ProductCountRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductCountViewModel(private val repository: ProductCountRepository) : ViewModel() {
    val cartCount: StateFlow<Int> = repository.cartCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val favouriteCount: StateFlow<Int> = repository.favouriteCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun addToCart(productId: String) {
        viewModelScope.launch { repository.addToCart(productId) }
    }

    fun removeOneFromCart(productId: String) {
        viewModelScope.launch { repository.removeOneFromCart(productId) }
    }

    fun addToFavourites(productId: String) {
        viewModelScope.launch { repository.addToFav(productId) }
    }

    fun removeFromFavourites(productId: String) {
        viewModelScope.launch { repository.removeFromFavourite(productId) }
    }

    fun quantityOf(productId: String) = repository.getQuantity(productId)

    fun isFavourite(productId: String) = repository.isInFav(productId)
}