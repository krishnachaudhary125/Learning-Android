package com.example.atry

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atry.databinding.ActivityShipptingAddressBinding

class ShippingAddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShipptingAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityShipptingAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.shippingAddressBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.addAddressNow.setOnClickListener {
            val intent = Intent(this, NewAddressActivity::class.java)
            startActivity(intent)
        }
    }
}