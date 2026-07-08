package com.example.atry.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.atry.R
import com.example.atry.data.models.Banner

class BannerRepository {

    private val _banners = MutableLiveData<List<Banner>>()
    val banners: LiveData<List<Banner>> = _banners

    fun fetchBanners() {

        _banners.value = listOf(
            Banner(
                id = 1,
                imageUrl = "android.resource://com.example.atry/${R.drawable.banner}",
                actionUrl = null
            ),
            Banner(
                id = 2,
                imageUrl = "android.resource://com.example.atry/${R.drawable.banner}",
                actionUrl = null
            ),
            Banner(
                id = 3,
                imageUrl = "android.resource://com.example.atry/${R.drawable.banner}",
                actionUrl = null
            )
        )
    }
}