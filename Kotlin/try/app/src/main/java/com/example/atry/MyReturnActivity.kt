package com.example.atry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.atry.databinding.ActivityReturnBinding

class MyReturnActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReturnBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReturnBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}