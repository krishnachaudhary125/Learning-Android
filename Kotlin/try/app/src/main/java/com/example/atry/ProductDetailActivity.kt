package com.example.atry

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.atry.data.models.Product
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

            optionVisibility(product,"Size", binding.sizeLabel, binding.sizeRadioBtn)
            optionVisibility(product, "Color", binding.colorLabel, binding.colorRadioBtn)
        }
    }
    fun optionVisibility(
        product: Product,
        optionName: String,
        label: View,
        radioGroup: View
    ) {
        val visibility = if (product.options.containsKey(optionName)) {
            View.VISIBLE
        } else {
            View.GONE
        }

        label.visibility = visibility
        radioGroup.visibility = visibility
    }
}