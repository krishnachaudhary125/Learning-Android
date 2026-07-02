package com.example.androidpractice.validation

data class ValidationResult(
    val valid: Boolean,
    val field: String,
    val message: String
)