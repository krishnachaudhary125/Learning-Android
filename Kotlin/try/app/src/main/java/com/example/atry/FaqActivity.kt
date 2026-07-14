package com.example.atry

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.atry.databinding.ActivityFaqBinding
import com.example.atry.ui.adapters.FaqAdapter
import com.example.atry.ui.viewmodel.MoreViewModel

class FaqActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFaqBinding
    private val viewModel: MoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFaqBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.faqBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val adapter = FaqAdapter { faq ->

        }

        binding.rvFaq.layoutManager = LinearLayoutManager(this)
        binding.rvFaq.adapter = adapter

        viewModel.faq.observe(this) { faqList ->
            adapter.submitList(faqList)
        }
    }
}