package com.example.atry.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.atry.data.models.Banner
import com.example.atry.data.repository.BannerRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val bannerRepository = BannerRepository()

    val banners: LiveData<List<Banner>> = bannerRepository.banners

    init {
        loadAllData()
    }

    private fun loadAllData() {
        loadBanners()
    }

    fun loadBanners() {
        viewModelScope.launch {
            bannerRepository.fetchBanners()
        }
    }
}