package com.example.eSewaMarket.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.Banner
import com.example.eSewaMarket.data.models.Category
import com.example.eSewaMarket.data.models.HotDeal
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.BannerRepository
import com.example.eSewaMarket.data.repository.CategoryRepository
import com.example.eSewaMarket.data.repository.HotDealCategoryRepository
import com.example.eSewaMarket.data.repository.ProductRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val bannerRepository = BannerRepository()
    private val categoryRepository = CategoryRepository()
    private val hotDealCategoryRepository = HotDealCategoryRepository()
    private val productRepository = ProductRepository()

    val banners: LiveData<List<Banner>> = bannerRepository.banners
    val category: LiveData<List<Category>> = categoryRepository.category
    val hotDealCategories: LiveData<List<HotDeal>> = hotDealCategoryRepository.hotDealCategories
    val products: LiveData<List<Product>> = productRepository.products

    init {
        loadAllData()
    }

    private fun loadAllData() {
        loadBanners()
        loadCategory()
        loadHotDealCategories()
        loadProduct()
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

    fun loadProduct() {
        viewModelScope.launch {
            productRepository.fetchProducts()
        }
    }
}