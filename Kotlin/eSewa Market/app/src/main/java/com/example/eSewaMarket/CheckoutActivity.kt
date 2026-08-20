package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.eSewaMarket.ui.compose.CheckoutScreen

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        setContent {
            CheckoutScreen(
                onBackClick = {
                    onBackPressedDispatcher.onBackPressed()
                }
            )
        }
    }
}