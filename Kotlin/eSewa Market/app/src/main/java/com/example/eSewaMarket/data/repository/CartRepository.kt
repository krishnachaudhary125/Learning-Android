package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.local.dao.CartDao
import com.example.eSewaMarket.data.local.entity.CartEntity
import com.example.eSewaMarket.data.models.AddToCartRequest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest

class CartRepository(
    private val cartDao: CartDao,
    private val userRepository: UserSessionRepository,
    private val apiService: ApiService
) {
    private suspend fun currentUserId(): Long {
        return userRepository.user.first().id
    }
    suspend fun addToCart(productId: Long) {

        val userId = currentUserId()
        val item = cartDao.getCartItem(userId, productId)

        val oldQuantity = item?.quantity?:0

        if (item == null) {
            cartDao.insert(
                CartEntity(userId = userId,
                    productId = productId,
                    quantity = 1
                )
            )
        } else {
            cartDao.updateQuantity(userId = userId,
                productId = productId,
                quantity = item.quantity + 1
            )
        }

        try {
            apiService.addToCart(
                AddToCartRequest(productId = productId)
            )
        }catch (e: Exception){
            if (oldQuantity == 0){
                cartDao.removeFromCart(
                    userId = userId,
                    productId = productId
                )
            }else{
                cartDao.updateQuantity(
                    userId = userId,
                    productId = productId,
                    quantity = oldQuantity
                )
            }
            throw e
        }
    }

    suspend fun removeOneFromCart(productId: Long) {

        val userId = currentUserId()
        val item = cartDao.getCartItem(userId, productId) ?: return

        if (item.quantity == 1) {
            cartDao.removeFromCart(userId, productId)
        } else {
            cartDao.updateQuantity(userId, productId, item.quantity - 1)
        }
    }

    fun totalQuantity() = userRepository.user.flatMapLatest { user ->
            cartDao.getTotalQuantity(user.id)
        }

    fun productQuantity(productId: Long) = userRepository.user.flatMapLatest { user ->
        cartDao.getProductQuantity(user.id, productId)
    }
}