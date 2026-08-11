package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.local.dao.FavouriteDao
import com.example.eSewaMarket.data.local.entity.FavouriteEntity
import com.example.eSewaMarket.data.models.AddToCartRequest
import com.example.eSewaMarket.data.models.FavouriteToggles
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await

class FavouriteRepository(
    private val favouriteDao: FavouriteDao,
    private val userRepository: UserSessionRepository,
    private val apiService: ApiService
) {
    private suspend fun currentUserId(): Long {
        return userRepository.user.first().id
    }

    suspend fun toggleFavourite(productId: Long) {
        val userId = currentUserId()

        val item = favouriteDao.getFavouriteItem(userId, productId)

        if (item == null) {
            favouriteDao.insert(
                FavouriteEntity(
                    userId = userId,
                    productId = productId
                )
            )
        } else {
            favouriteDao.removeFromFavourite(userId, productId)
        }

        val token = FirebaseAuth.getInstance()
            .currentUser
            ?.getIdToken(false)
            ?.await()
            ?.token
            ?: throw IllegalStateException("User is not authenticated")

        apiService.toggleFavourite(
            "Bearer $token",
            FavouriteToggles(productId = productId)
        )
    }

    fun totalQuantity() = userRepository.user.flatMapLatest { user ->
        favouriteDao.getFavouriteCount(user.id)
    }

    fun isFavourite(productId: Long): Flow<Boolean> {
        return userRepository.user.flatMapLatest { user ->
            favouriteDao.isFavourite(user.id, productId)
        }
    }
}