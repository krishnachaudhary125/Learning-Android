package com.example.atry

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.atry.databinding.ActivityProductDetailBinding
import com.example.atry.ui.adapters.ProductImageAdapter
import com.example.atry.ui.viewmodel.ProductDetailViewModel

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()

    private lateinit var imageGalleryAdapter: ProductImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.productDetailBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val productId = intent.getIntExtra("product_id", -1)
        if(productId == -1){
            finish()
            return
        }

        setupImageGallery()
        observeProduct()

        viewModel.loadProduct(productId)
    }

    private fun setupImageGallery(){
        imageGalleryAdapter = ProductImageAdapter()
        binding.rvProductImage.apply {
            layoutManager = LinearLayoutManager(
                this@ProductDetailActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = imageGalleryAdapter
        }
    }

    private fun observeProduct(){
        viewModel.selectedProduct.observe(this){ product ->
            binding.productName.text = product.title
            binding.productPrice.text = "Rs.${product.price}"
            if(product.stock != 0){
                binding.productStock.text = "In Stock"
            }
            else{
                binding.productStock.text = "Out of Stock"
            }
            binding.productDescription.text = product.description
            imageGalleryAdapter.submitList(product.images)
        }
    }
}