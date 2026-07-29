package com.example.eSewaMarket.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eSewaMarket.data.models.Banner
import com.example.eSewaMarket.data.models.Category
import com.example.eSewaMarket.data.models.HotDeal
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.models.ProductResponse
import com.example.eSewaMarket.data.repository.BannerRepository
import com.example.eSewaMarket.data.repository.CategoryRepository
import com.example.eSewaMarket.data.repository.HotDealCategoryRepository
import com.example.eSewaMarket.data.repository.ProductRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class HomeViewModel : ViewModel() {

    private val bannerRepository = BannerRepository()
    private val categoryRepository = CategoryRepository()
    private val hotDealCategoryRepository = HotDealCategoryRepository()
    private val productRepository = ProductRepository()

    val banners: LiveData<List<Banner>> = bannerRepository.banners
    val category: LiveData<List<Category>> = categoryRepository.category
    val hotDealCategories: LiveData<List<HotDeal>> = hotDealCategoryRepository.hotDealCategories
    val products: LiveData<List<Product>> = productRepository.products

    private val RECOMMENDED_PAGE_SIZE = 8

    private val _recommendedProducts = MutableLiveData<List<ProductResponse>>(emptyList())
    val recommendedProducts: LiveData<List<ProductResponse>> = _recommendedProducts

    private val _recommendedLoading = MutableLiveData(false)
    val recommendedLoading: LiveData<Boolean> = _recommendedLoading

    private var recommendedPage = 0
    private var isLoadingRecommended = false
    private var isLastRecommendedPage = false

    init {
        loadAllData()
    }

    private fun loadAllData() {
        loadBanners()
        loadCategory()
        loadHotDealCategories()
        loadProduct()
        loadMoreRecommended()
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

    fun loadMoreRecommended() {
        if (isLoadingRecommended || isLastRecommendedPage) return

        isLoadingRecommended = true
        _recommendedLoading.value = true

        viewModelScope.launch {
            try {
                val response = productRepository.fetchRecommendedProducts(
                    page = recommendedPage,
                    size = RECOMMENDED_PAGE_SIZE
                )

                delay(1000)

                val current = _recommendedProducts.value.orEmpty()
                _recommendedProducts.value = current + response.content

                isLastRecommendedPage = response.last
                recommendedPage++

            } catch (e: Exception) {
                Log.e("API_ERROR", "loadMoreRecommended failed", e)
            } finally {
                isLoadingRecommended = false
                _recommendedLoading.value = false
            }
        }
    }
}