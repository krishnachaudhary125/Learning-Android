package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eSewaMarket.data.models.Product
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.ActivityFeaturedProductsBinding
import com.example.eSewaMarket.databinding.ItemProductBinding
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.ui.viewmodel.HomeViewModel
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.utils.SnackBarUtil
import kotlinx.coroutines.launch

class FeaturedProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeaturedProductsBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(this)
    }

    private val favouriteViewModel: FavouriteViewModel by viewModels {
        ViewModelFactoryProvider.favouriteFactory(this)
    }
    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityFeaturedProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.tbFeaturedProducts.toolbarBackTitle) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        binding.tbFeaturedProducts.toolbarTitle.text = "Featured Products"

        binding.tbFeaturedProducts.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        userSessionRepository = UserSessionRepository(this)
        authNavigator = AuthNavigator(userSessionRepository)

        homeViewModel.home.observe(this) { home ->
            binding.composeView.setContent {
                FeaturedProductScreen(
                    products = home.featuredProducts,
                    cartViewModel = cartViewModel,
                    favouriteViewModel = favouriteViewModel
                )
            }
        }
    }

    @Composable
    fun FeaturedProductScreen(
        products: List<Product>,
        cartViewModel: CartViewModel,
        favouriteViewModel: FavouriteViewModel
    ){
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ItemProductCard(
                    product = product,
                    cartViewModel = cartViewModel,
                    favouriteViewModel = favouriteViewModel,
                    onClick = { product ->
                        val intent = Intent(this@FeaturedProductActivity, ProductDetailActivity::class.java)
                        intent.putExtra("product_id", product.id)
                        startActivity(intent)
                    },
                    onAddToCartClick = {
                        lifecycleScope.launch {
                            if (authNavigator.isLoggedIn()){
                                cartViewModel.addToCart(product)
                            }
                            else{
                                SnackBarUtil.show(
                                    view = binding.root,
                                    context = this@FeaturedProductActivity,
                                    text = "Login to continue.",
                                    actionText = "GO TO LOGIN"
                                ){
                                    val intent = Intent(this@FeaturedProductActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    },
                    onRemoveOneFromCart = { product ->
                        lifecycleScope.launch {
                            if (authNavigator.isLoggedIn()){
                                cartViewModel.removeOneFromCart(product.id)
                            }
                        }
                    },
                    onFavouriteClick = { product ->
                        lifecycleScope.launch {
                            if (authNavigator.isLoggedIn()){
                                favouriteViewModel.toggleFavourite(product.id)
                            }
                            else{
                                SnackBarUtil.show(
                                    view = binding.root,
                                    context = this@FeaturedProductActivity,
                                    text = "Login to continue.",
                                    actionText = "GO TO LOGIN"
                                ){
                                    val intent = Intent(this@FeaturedProductActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    fun ItemProductCard(
        product: Product,
        cartViewModel: CartViewModel,
        onClick: (Product) -> Unit,
        favouriteViewModel: FavouriteViewModel,
        onAddToCartClick: (Product) -> Unit,
        onRemoveOneFromCart: (Product) -> Unit,
        onFavouriteClick: (Product) -> Unit
    ){
        val quantity by cartViewModel
            .productQuantity(product.id)
            .collectAsState(initial = 0)

        val isFavourite by favouriteViewModel
            .isFavourite(product.id)
            .collectAsState(initial = false)

        AndroidView(
            factory = { context ->
                ItemProductBinding.inflate(
                    LayoutInflater.from(context)
                ).root
            },
            update = { view ->
                val binding = ItemProductBinding.bind(view)

                binding.apply {
                    productTitle.text = product.title
                    brand.text = product.brand
                    price.text = product.price.toString()

                    Glide.with(productImage.context)
                        .load(product.thumbnail)
                        .into(productImage)

                    numOfProduct.text = quantity.toString()
                    val visibility = if(quantity > 0) View.VISIBLE else View.GONE
                    numOfProduct.visibility = visibility
                    minusProduct.visibility = visibility

                    favourite.setImageResource(
                        if (isFavourite)
                            R.drawable.ic_fav_filled
                        else
                            R.drawable.ic_fav
                    )

                    imageContainer.setOnClickListener {
                        onClick(product)
                    }

                    plusProduct.setOnClickListener {
                        onAddToCartClick(product)
                    }

                    minusProduct.setOnClickListener {
                        onRemoveOneFromCart(product)
                    }

                    favourite.setOnClickListener {
                        onFavouriteClick(product)
                    }
                }
            }
        )
    }
}