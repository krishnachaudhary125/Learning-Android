package com.example.eSewaMarket.data.models

data class UserResponse(
    val id: Long,
    val firebaseUid: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val photo: String?,
    val emailVerified: Boolean
)
