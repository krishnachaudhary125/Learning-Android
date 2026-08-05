package com.example.eSewaMarket

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.eSewaMarket.databinding.ActivityMainBinding
import com.example.eSewaMarket.data.models.NavItem
import com.example.eSewaMarket.data.repository.ProductCountRepository
import com.example.eSewaMarket.ui.fragments.CartFragment
import com.example.eSewaMarket.ui.fragments.FavouriteFragment
import com.example.eSewaMarket.ui.fragments.HomeFragment
import com.example.eSewaMarket.ui.fragments.MoreFragment
import com.example.eSewaMarket.ui.viewmodel.ProductCountViewModel
import com.example.eSewaMarket.ui.viewmodel.ProductCountViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var selectedTab = 1

    private val productCountViewModel: ProductCountViewModel by viewModels {
        ProductCountViewModelFactory(
            ProductCountRepository(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        if(intent.getBooleanExtra("openHome", false)){
            loadFragment(HomeFragment())
        }

        val shop = NavItem(binding.bottomNav.shopButton, binding.bottomNav.shopLabel, binding.bottomNav.shopIcon)
        val cart = NavItem(binding.bottomNav.cartButton, binding.bottomNav.cartLabel, binding.bottomNav.cartIcon)
        val favourite = NavItem(binding.bottomNav.favouriteButton, binding.bottomNav.favouriteLabel, binding.bottomNav.favouriteIcon)
        val more = NavItem(binding.bottomNav.moreButon, binding.bottomNav.moreLabel, binding.bottomNav.moreIcon)

        val fragmentToOpen = intent.getStringExtra("open_fragment")
        when (fragmentToOpen) {
            "cart" -> openFragment(CartFragment(), cart, shop, favourite, more, 2)
            "favourite" -> openFragment(FavouriteFragment(), favourite, shop, cart, more, 3)
        }

        binding.bottomNav.shopButton.setOnClickListener {
            if (selectedTab != 1) {
                openFragment(HomeFragment() ,shop, cart, favourite, more, 1)
            }
        }

        binding.bottomNav.cartButton.setOnClickListener {
            if (selectedTab != 2) {
                openFragment(CartFragment(), cart, shop, favourite, more, 2)
            }
        }

        binding.bottomNav.favouriteButton.setOnClickListener {
            if (selectedTab != 3) {
                openFragment(FavouriteFragment(), favourite, shop, cart, more, 3)
            }
        }

        binding.bottomNav.moreButon.setOnClickListener {
            if (selectedTab != 4) {
                openFragment(MoreFragment(), more, shop, cart, favourite, 4)
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productCountViewModel.cartCount.collect { count ->
                    if (count > 0){
                        binding.bottomNav.numOfProductInCart.text = count.toString()
                        binding.bottomNav.numOfProductInCart.visibility = View.VISIBLE
                    }else{
                        binding.bottomNav.numOfProductInCart.visibility = View.GONE
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productCountViewModel.favouriteCount.collect { count ->

                    binding.bottomNav.numOfProductInFavourite.visibility =
                        if (count > 0) View.VISIBLE else View.GONE

                    binding.bottomNav.numOfProductInFavourite.text = count.toString()
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrame, fragment)
            .commit()
    }


    private fun onSelect(item: NavItem) {
        item.label.visibility = View.VISIBLE
        item.icon.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
        item.button.setBackgroundResource(R.drawable.navigation_background)
        item.button.animate()
            .scaleX(1.1f)
            .scaleY(1.1f)
            .setDuration(100)
            .withEndAction {
                item.button.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
            }
    }

    private fun onDeselect(item: NavItem) {
        item.label.visibility = View.GONE
        item.icon.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
        item.button.setBackgroundResource(android.R.color.transparent)
    }

    fun openFragment(fragment: Fragment, select: NavItem, deselect1: NavItem, deselect2: NavItem, deselect3: NavItem, selectedTabValue: Int){
        loadFragment(fragment)

        onSelect(select)
        onDeselect(deselect1)
        onDeselect(deselect2)
        onDeselect(deselect3)

        selectedTab = selectedTabValue
    }
}