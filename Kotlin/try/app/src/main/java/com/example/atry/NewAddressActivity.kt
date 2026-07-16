package com.example.atry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atry.databinding.ActivityNewAddressBinding

class NewAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newShippingAddressBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}