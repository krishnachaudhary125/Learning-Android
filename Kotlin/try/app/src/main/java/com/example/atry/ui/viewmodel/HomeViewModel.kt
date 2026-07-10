package com.example.atry.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atry.data.models.Banner
import com.example.atry.data.models.Category
import com.example.atry.data.models.HotDeal
import com.example.atry.data.repository.BannerRepository
import com.example.atry.data.repository.CategoryRepository
import com.example.atry.data.repository.HotDealCategoryRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val bannerRepository = BannerRepository()
    private val categoryRepository = CategoryRepository()

    private val hotDealCategoryRepository = HotDealCategoryRepository()


    val banners: LiveData<List<Banner>> = bannerRepository.banners
    val category: LiveData<List<Category>> = categoryRepository.category

    val hotDealCategories: LiveData<List<HotDeal>> = hotDealCategoryRepository.hotDealCategories

    init {
        loadAllData()
    }

    private fun loadAllData() {
        loadBanners()
        loadCategory()
        loadHotDealCategories()
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

    fun loadHotDealCategories() {
        viewModelScope.launch {
            hotDealCategoryRepository.fetchHotDealCategories()
        }
    }
}