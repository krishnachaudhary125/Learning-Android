package com.example.eSewaMarket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class FavouriteViewModel(
    private val repository: FavouriteRepository
) : ViewModel() {

    fun favouriteCount() = repository.totalQuantity()

    fun toggleFavourite(product: Product) {
        viewModelScope.launch {
            repository.toggleFavourite(product)
        }
    }

    fun isFavourite(productId: Long): Flow<Boolean> {
        return repository.isFavourite(productId)
    }

    fun favouriteProducts() : Flow<List<FavouriteResponse>>{
        return repository.favouriteProducts()
    }

    suspend fun deleteAllFavourites() {
        repository.deleteFavourites()
    }

    fun removeOne(productId: Long){
        viewModelScope.launch {
            repository.removeOneFromFavourite(productId)
        }
    }

    fun syncFavouritesWithServer() {
        viewModelScope.launch {
            try {
                repository.syncFavouritesWithServer()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("SYNC_Favourite", "Favourite sync failed", e)
            }
        }
    }
}