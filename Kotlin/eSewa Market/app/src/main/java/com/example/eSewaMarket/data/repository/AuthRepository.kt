package com.example.eSewaMarket.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.first

class AuthRepository(
    private val userSessionRepository: UserSessionRepository,
) {
    suspend fun logout(){
        FirebaseAuth.getInstance().signOut()
        userSessionRepository.logout()
    }
}