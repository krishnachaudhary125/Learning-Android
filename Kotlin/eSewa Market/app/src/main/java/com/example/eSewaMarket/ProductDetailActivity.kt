package com.example.eSewaMarket

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.ProductCountRepository
import com.example.eSewaMarket.databinding.ActivityProductDetailBinding
import com.example.eSewaMarket.ui.adapters.OptionAdapter
import com.example.eSewaMarket.ui.adapters.ProductAdapter
import com.example.eSewaMarket.ui.adapters.ProductImageAdapter
import com.example.eSewaMarket.ui.adapters.SimilarProductAdapter
import com.example.eSewaMarket.ui.viewmodel.ProductCountViewModel
import com.example.eSewaMarket.ui.viewmodel.ProductCountViewModelFactory
import com.example.eSewaMarket.ui.viewmodel.ProductDetailViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private lateinit var imageGalleryAdapter: ProductImageAdapter
    private lateinit var similarProductAdapter: SimilarProductAdapter
    private val optionAdapters = mutableMapOf<String, OptionAdapter>()
    private val productCountViewModel: ProductCountViewModel by viewModels {
        ProductCountViewModelFactory(
            ProductCountRepository(applicationContext)
        )
    }
    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarProductDetail.toolbarBackAction) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        id = intent.getIntExtra("product_id", -1)

        binding.toolbarProductDetail.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.toolbarProductDetail.toolbarIcon.setImageResource(R.drawable.ic_cart)
        binding.toolbarProductDetail.toolbarIcon.setBackgroundResource(R.drawable.bg_cart)

        val screenWidth = resources.displayMetrics.widthPixels
        val desiredWidth = (screenWidth * 0.45f).toInt()

        val maxWidth = (180 * resources.displayMetrics.density).toInt()
        val width = minOf(desiredWidth, maxWidth)

        similarProductAdapter = SimilarProductAdapter(
            productCountViewModel,
            width
        ) { product ->
            startActivity(
                Intent(this, ProductDetailActivity::class.java)
                    .putExtra("product_id", product.id)
            )
        }

        val productId = intent.getIntExtra("product_id", -1)
        if(productId == -1){
            finish()
            return
        }

        setupImageGallery()
        setupOptionRecyclerView("Size", binding.rvSizeOption)
        observeProduct()
        setupSimilarProductRecyclerView()

        viewModel.loadProduct(productId)
        viewModel.loadSimilarProducts()
        observeCartQuantity()
        observeFavouriteQuantity()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productCountViewModel.cartCount.collect { count ->
                    if (count > 0){
                        binding.toolbarProductDetail.numOfProductInCart.text = count.toString()
                        binding.toolbarProductDetail.numOfProductInCart.visibility = View.VISIBLE
                    }else{
                        binding.toolbarProductDetail.numOfProductInCart.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.similarProducts.observe(this) { products ->
            similarProductAdapter.submitList(products.take(5))
            binding.rvSimilarProduct.post {
                binding.rvSimilarProduct.scrollToPosition(0)
            }
        }
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

    private fun setupSimilarProductRecyclerView() {

        binding.rvSimilarProduct.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.rvSimilarProduct.adapter = similarProductAdapter
    }

    private fun observeProduct(){
        viewModel.selectedProduct.observe(this){ product ->
            binding.productName.text = product.title
            binding.productPrice.text = "Rs.${product.price}"
            if(product.stock != 0){
                binding.productStock.text = "In Stock"
                binding.productStock.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
                binding.addToCartBG.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.green)
                )
                binding.bottomAddToCartBtn.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.white)
                )
                binding.bottomAddToCartBtn.isEnabled = true
            }
            else{
                binding.productStock.text = "Out of Stock"
                binding.productStock.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.red)
                )
                binding.addToCartBG.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(binding.root.context, R.color.addToCartSoldOut)
                )
                binding.bottomAddToCartBtn.setTextColor(
                    ContextCompat.getColor(binding.root.context, R.color.text_dark)
                )
                binding.bottomAddToCartBtn.isEnabled = false
            }
            binding.productDescription.text = product.description
            setRating(product.rating.toFloat())
            imageGalleryAdapter.submitList(product.images)

            binding.vpProductImage.adapter = imageGalleryAdapter

            if (imageGalleryAdapter.itemCount > 1){
                TabLayoutMediator(binding.imageDotIndicator, binding.vpProductImage){_,_ -> }.attach()
            }

            optionVisibility(product,"Size", binding.sizeLabel, binding.rvSizeOption)
            optionVisibility(product, "Color", binding.colorLabel, binding.colorRadioBtn)

            optionAdapters["Size"]?.submitList(product.options["Size"] ?: emptyList())

            binding.productTitle.text = product.title
            binding.bottomProductPrice.text = "Rs.${product.price}"

            if(product.discountPercentage != null){
                val discountAmount =
                    (product.discountPercentage?.times(product.price.toDouble()) ?: 0.0) / 100

                val priceBeforeDiscount = product.price.toDouble() + discountAmount

                val strikedPrice = SpannableString("Rs. ${String.format("%.2f", priceBeforeDiscount)}")
                strikedPrice.setSpan(
                    StrikethroughSpan(),
                    0,
                    strikedPrice.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.originalPrice.text = strikedPrice
                binding.originalPrice.visibility = View.VISIBLE
            }else{
                binding.originalPrice.visibility = View.GONE
            }

            binding.toolbarProductDetail.toolbarIcon.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("open_fragment", "cart")
                startActivity(intent)
            }

            binding.bottomAddToCartBtn.setOnClickListener {
                productCountViewModel.addToCart(id.toString())
            }

            binding.plusProductBtn.setOnClickListener {
                productCountViewModel.addToCart(id.toString())
            }

            binding.minusProductBtn.setOnClickListener {
                productCountViewModel.removeOneFromCart(id.toString())
            }

            binding.favBtn.setOnClickListener {
                favouriteJob = lifecycleScope.launch {
                    val isFav = productCountViewModel.isFavourite(id.toString()).first()

                    if (isFav) {

                        productCountViewModel.removeFromFavourites(id.toString())
                    } else {
                        productCountViewModel.addToFavourites(id.toString())
                    }
                }
            }

            binding.avgRating.text = product.rating.toString()
            binding.totalReviews.text = product.reviewCount.toString()
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

    private var quantityJob: Job? = null
    private fun observeCartQuantity() {
        quantityJob?.cancel()

        quantityJob = lifecycleScope.launch {
            productCountViewModel.quantityOf(id.toString()).collect { qty ->

                binding.tvCartCount.text = qty.toString()

                val show = if (qty == 0) View.VISIBLE else View.GONE
                binding.bottomAddToCartBtn.visibility = show

                val visible = if (qty > 0) View.VISIBLE else View.GONE
                binding.plusProductBtn.visibility = visible
                binding.tvCartCount.visibility = visible
                binding.minusProductBtn.visibility = visible
            }
        }
    }

    private var favouriteJob: Job? = null
    private fun observeFavouriteQuantity() {
        favouriteJob?.cancel()

        favouriteJob = lifecycleScope.launch {
            productCountViewModel.isFavourite(id.toString()).collect { isFav ->
                binding.favBtn.setImageResource(
                    if (isFav)
                        R.drawable.ic_fav_filled_white
                    else
                        R.drawable.ic_fav
                )
            }
        }
    }

    private fun setRating(rating: Float){
        val rounded = (rating * 2).roundToInt() / 2f

        val stars = listOf(
            binding.ratingStar.star1,
            binding.ratingStar.star2,
            binding.ratingStar.star3,
            binding.ratingStar.star4,
            binding.ratingStar.star5
        )
        
        stars.forEachIndexed { index, view ->
            val value = rounded -  index

            view.setImageResource(
                when{
                    value >= 1f -> R.drawable.ic_star_filled
                    value >= 0.5f -> R.drawable.ic_star_half
                    else -> R.drawable.ic_star_empty
                }
            )
        }
    }
}