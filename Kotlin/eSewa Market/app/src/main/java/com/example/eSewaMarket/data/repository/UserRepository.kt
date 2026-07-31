package com.example.eSewaMarket.data.repository

import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.models.UserSyncRequest

class UserRepository {

    suspend fun syncUser(
        token: String,
        request: UserSyncRequest
    ) = RetrofitInstance.api.syncUser(
        "Bearer $token",
        request
    )
}