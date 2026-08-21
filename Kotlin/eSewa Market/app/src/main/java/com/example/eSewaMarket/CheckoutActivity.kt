package com.example.eSewaMarket

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.ui.compose.CheckoutScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import kotlin.getValue

class CheckoutActivity : AppCompatActivity() {

    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        setContent {
            val products by cartViewModel.cartProducts()
                .collectAsStateWithLifecycle(
                    initialValue = emptyList()
                )
            CheckoutScreen(
                checkoutProducts = products,
                onBackClick = {
                    onBackPressedDispatcher.onBackPressed()
                },
                totalPrice = 19500.00,
                itemCount = 5,
                productPrice = 18000.00,
                totalTax = 1450.00,
                shippingCharge = 50.00,
                address = "Pulchowk, Lalitpur - 20",
                promoBtn = {},
                onProductClick = {}
            )
        }
    }
}