package com.example.atry.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atry.data.models.Banner
import com.example.atry.data.models.Category
import com.example.atry.data.repository.BannerRepository
import com.example.atry.data.repository.CategoryRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val bannerRepository = BannerRepository()
    private val categoryRepository = CategoryRepository()

    val banners: LiveData<List<Banner>> = bannerRepository.banners
    val category: LiveData<List<Category>> = categoryRepository.category

    init {
        loadAllData()
    }

    private fun loadAllData() {
        loadBanners()
        loadCategory()
    }

    fun loadBanners() {
        viewModelScope.launch {
            bannerRepository.fetchBanners()
        }
    }

    fun loadCategory() {
        viewModelScope.launch {
            categoryRepository.fetchCategory()
        }
    }
}