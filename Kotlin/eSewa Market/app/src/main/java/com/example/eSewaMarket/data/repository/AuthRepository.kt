package com.example.eSewaMarket.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val userSessionRepository: UserSessionRepository,
    private val cartRepository: CartRepository
) {
    suspend fun logout(){
        val userId = userSessionRepository.user.first().id
        FirebaseAuth.getInstance().signOut()
        userSessionRepository.logout()
        cartRepository.clearCart(userId)
    }
}