package com.example.atry

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.atry.data.models.Product
import com.example.atry.databinding.ActivityProductDetailBinding
import com.example.atry.ui.adapters.OptionAdapter
import com.example.atry.ui.adapters.ProductImageAdapter
import com.example.atry.ui.viewmodel.ProductDetailViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private lateinit var imageGalleryAdapter: ProductImageAdapter
    private val optionAdapters = mutableMapOf<String, OptionAdapter>()


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
        setupOptionRecyclerView("Size", binding.rvSizeOption)
        observeProduct()

        binding.vpProductImage.adapter = imageGalleryAdapter

        viewModel.loadProduct(productId)
    }

    private fun setupImageGallery(){
        imageGalleryAdapter = ProductImageAdapter()
        binding.vpProductImage.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = true
        }
    }

    private fun setupOptionRecyclerView(
        optionName: String,
        recyclerView: RecyclerView
    ) {
        val adapter = OptionAdapter()
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        optionAdapters[optionName] = adapter
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

            optionVisibility(product,"Size", binding.sizeLabel, binding.rvSizeOption)
            optionVisibility(product, "Color", binding.colorLabel, binding.colorRadioBtn)

            optionAdapters["Size"]?.submitList(product.options["Size"] ?: emptyList())
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