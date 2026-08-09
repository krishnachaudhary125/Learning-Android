package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.ApiService
import com.example.eSewaMarket.data.local.dao.CartDao
import com.example.eSewaMarket.data.local.entity.CartEntity
import com.example.eSewaMarket.data.models.AddToCartRequest
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

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
                CartEntity(userId, productId, quantity = 1)
            )
        } else {
            cartDao.updateQuantity(userId, productId, item.quantity + 1
            )
        }

        try {
            val token = FirebaseAuth.getInstance()
                .currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?: throw IllegalStateException("User is not authenticated")

            apiService.addToCart(
                "Bearer $token",
                AddToCartRequest(productId = productId)
            )
        }catch (e: Exception){
            if (oldQuantity == 0){
                cartDao.removeFromCart(userId, productId)
            }else{
                cartDao.updateQuantity(userId, productId, oldQuantity)
            }
            throw e
        }
    }

    suspend fun removeOneFromCart(productId: Long) {

        val userId = currentUserId()
        val item = cartDao.getCartItem(userId, productId) ?: return
        val oldQuantity = item?.quantity?:0

        if (item.quantity == 1) {
            cartDao.removeFromCart(userId, productId)
        } else {
            cartDao.updateQuantity(userId, productId, item.quantity - 1)
        }

        try {
            val token = FirebaseAuth.getInstance()
                .currentUser
                ?.getIdToken(false)
                ?.await()
                ?.token
                ?: throw IllegalStateException("User is not authenticated")

            apiService.removeOneFromCart(
                "Bearer $token",
                productId
            )
        }catch (e: Exception){
            if (oldQuantity == 1){
                cartDao.removeFromCart(userId, productId)
            }else{
                cartDao.updateQuantity(userId, productId, oldQuantity)
            }
            throw e
        }
    }

    fun totalQuantity() = userRepository.user.flatMapLatest { user ->
            cartDao.getTotalQuantity(user.id)
        }

    fun productQuantity(productId: Long) = userRepository.user.flatMapLatest { user ->
        cartDao.getProductQuantity(user.id, productId)
    }

    suspend fun clearCart(userId: Long) {
        withContext(Dispatchers.IO) {
            cartDao.clearCart(userId)
        }
    }
}