package com.example.atry.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.atry.data.repository.ProductCountRepository

class ProductCountViewModelFactory(private val repository: ProductCountRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ProductCountViewModel(repository) as T
    }
}