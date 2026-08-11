package com.example.eSewaMarket

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.databinding.ActivityMainBinding
import com.example.eSewaMarket.data.models.NavItem
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.data.repository.FavouriteRepository
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.factory.CartViewModelFactory
import com.example.eSewaMarket.ui.factory.FavouriteViewModelFactory
import com.example.eSewaMarket.ui.fragments.CartFragment
import com.example.eSewaMarket.ui.fragments.FavouriteFragment
import com.example.eSewaMarket.ui.fragments.HomeFragment
import com.example.eSewaMarket.ui.fragments.MoreFragment
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import kotlinx.coroutines.launch
import kotlin.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var selectedTab = 1
    private val tabHistory = ArrayDeque<Int>()
    private lateinit var shop: NavItem
    private lateinit var cart: NavItem
    private lateinit var favourite: NavItem
    private lateinit var more: NavItem
    private val cartViewModel: CartViewModel by viewModels {
        val app = this.application as EsewaMarketApplication

        CartViewModelFactory(
            CartRepository(
                app.database.cartDao(),
                UserSessionRepository(app.applicationContext),
                RetrofitInstance.api
            )
        )
    }
    private val favouriteViewModel: FavouriteViewModel by viewModels {
        val app = this.application as EsewaMarketApplication

        FavouriteViewModelFactory(
            FavouriteRepository(
                app.database.favouriteDao(),
                UserSessionRepository(app.applicationContext),
                RetrofitInstance.api
            )
        )
    }
    private val homeFragment = HomeFragment()
    private val cartFragment = CartFragment()
    private val favouriteFragment = FavouriteFragment()
    private val moreFragment = MoreFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shop = NavItem(binding.bottomNav.shopButton, binding.bottomNav.shopLabel, binding.bottomNav.shopIcon)
        cart = NavItem(binding.bottomNav.cartButton, binding.bottomNav.cartLabel, binding.bottomNav.cartIcon)
        favourite = NavItem(binding.bottomNav.favouriteButton, binding.bottomNav.favouriteLabel, binding.bottomNav.favouriteIcon)
        more = NavItem(binding.bottomNav.moreButon, binding.bottomNav.moreLabel, binding.bottomNav.moreIcon)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainFrame, homeFragment)
                .commit()

            activeFragment = homeFragment
            onSelect(shop)
        }

        if(intent.getBooleanExtra("openHome", false)){
            switchFragment(homeFragment)
        }

        if (intent.getBooleanExtra("login_success", false)) {
            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
        }

        handleIntent(intent)

        binding.bottomNav.shopButton.setOnClickListener {
            if (selectedTab != 1) {
                navigateToTab(homeFragment ,shop, cart, favourite, more, 1)
            }
        }

        binding.bottomNav.cartButton.setOnClickListener {
            if (selectedTab != 2) {
                navigateToTab(cartFragment, cart, shop, favourite, more, 2)
            }
        }

        binding.bottomNav.favouriteButton.setOnClickListener {
            if (selectedTab != 3) {
                navigateToTab(favouriteFragment, favourite, shop, cart, more, 3)
            }
        }

        binding.bottomNav.moreButon.setOnClickListener {
            if (selectedTab != 4) {
                navigateToTab(moreFragment, more, shop, cart, favourite, 4)
            }
        }

        onBackPressedDispatcher.addCallback(this) {

            if (tabHistory.isNotEmpty()) {

                when (tabHistory.removeLast()) {
                    1 -> showTab(homeFragment, shop, cart, favourite, more, 1)

                    2 -> showTab(cartFragment, cart, shop, favourite, more, 2)

                    3 -> showTab(favouriteFragment, favourite, shop, cart, more, 3)

                    4 -> showTab(moreFragment, more, shop, cart, favourite, 4)
                }

            } else {
                finish()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                cartViewModel.cartCount().collect { count ->
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
                favouriteViewModel.favouriteCount().collect { count ->

                    binding.bottomNav.numOfProductInFavourite.visibility =
                        if (count > 0) View.VISIBLE else View.GONE

                    binding.bottomNav.numOfProductInFavourite.text = count.toString()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private var activeFragment: Fragment = homeFragment
    private fun switchFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        if (!fragment.isAdded) {
            transaction.hide(activeFragment)
                .add(R.id.mainFrame, fragment)
        } else {
            transaction.hide(activeFragment)
                .show(fragment)
        }

        transaction.commit()
        activeFragment = fragment
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

    private fun navigateToTab(
        fragment: Fragment,
        select: NavItem,
        deselect1: NavItem,
        deselect2: NavItem,
        deselect3: NavItem,
        selectedTabValue: Int
    ) {
        if (selectedTab != selectedTabValue) {
            tabHistory.addLast(selectedTab)
        }

        showTab(fragment, select, deselect1, deselect2, deselect3, selectedTabValue
        )
    }

    private fun showTab(
        fragment: Fragment,
        select: NavItem,
        deselect1: NavItem,
        deselect2: NavItem,
        deselect3: NavItem,
        selectedTabValue: Int
    ) {
        switchFragment(fragment)

        onSelect(select)
        onDeselect(deselect1)
        onDeselect(deselect2)
        onDeselect(deselect3)

        selectedTab = selectedTabValue
    }

    private fun handleIntent(intent: Intent?) {
        when (intent?.getStringExtra("open_fragment")) {
            "home" -> navigateToTab(homeFragment, shop, cart, favourite, more, 1)
            "cart" -> navigateToTab(cartFragment, cart, shop, favourite, more, 2)
            "favourite" -> navigateToTab(favouriteFragment, favourite, shop, cart, more, 3)
        }
    }
}