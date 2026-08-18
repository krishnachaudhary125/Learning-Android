package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.data.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavouriteViewModel(
    private val repository: FavouriteRepository
) : ViewModel() {

    fun favouriteCount() = repository.totalQuantity()

    fun toggleFavourite(productId: Long) {
        viewModelScope.launch {
            repository.toggleFavourite(productId)
        }
    }

    fun isFavourite(productId: Long): Flow<Boolean> {
        return repository.isFavourite(productId)
    }

    fun favouriteProducts() : Flow<List<FavouriteResponse>>{
        return repository.favouriteProducts()
    }

    suspend fun deleteAllFavourites() {
        viewModelScope.launch {
            repository.deleteFavourites()
        }
    }
}