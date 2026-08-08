package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.local.dao.FavouriteDao
import com.example.eSewaMarket.data.local.entity.FavouriteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest

class FavouriteRepository(
    private val favouriteDao: FavouriteDao,
    private val userRepository: UserSessionRepository
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