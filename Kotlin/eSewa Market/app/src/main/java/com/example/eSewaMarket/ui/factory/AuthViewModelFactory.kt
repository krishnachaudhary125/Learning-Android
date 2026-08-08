package com.example.eSewaMarket.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.local.AppDatabase
import com.example.eSewaMarket.data.repository.AuthRepository
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.viewmodel.AuthViewModel

class AuthViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}