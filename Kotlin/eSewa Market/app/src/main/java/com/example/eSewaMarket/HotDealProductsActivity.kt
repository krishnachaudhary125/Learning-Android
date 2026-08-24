package com.example.eSewaMarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.eSewaMarket.databinding.ActivityHotDealProductsBinding

class HotDealProductsActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHotDealProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityHotDealProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}