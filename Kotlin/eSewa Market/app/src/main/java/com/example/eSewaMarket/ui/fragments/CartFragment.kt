package com.example.eSewaMarket.ui.fragments

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.eSewaMarket.LoginActivity
import com.example.eSewaMarket.MainActivity
import com.example.eSewaMarket.ProductDetailActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.repository.ProductCountRepository
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.FragmentCartBinding
import com.example.eSewaMarket.ui.adapters.RecommendedProductAdapter
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.HomeViewModel
import com.example.eSewaMarket.ui.viewmodel.ProductCountViewModel
import com.example.eSewaMarket.ui.viewmodel.ProductCountViewModelFactory
import com.example.eSewaMarket.utils.AuthNavigator
import com.example.eSewaMarket.utils.SnackBarUtil
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.getValue

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by viewModels()
    private val productCountViewModel: ProductCountViewModel by viewModels {
        ProductCountViewModelFactory(ProductCountRepository(requireContext().applicationContext))
    }
    private lateinit var recommendedAdapter: RecommendedProductAdapter
    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        userSessionRepository = UserSessionRepository(requireContext())
        authNavigator = AuthNavigator(userSessionRepository)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarCart.toolBarCartFragment){ view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )
            insets
        }

        binding.toolbarCart.toolbarTitle.text = "My Cart"
        binding.toolbarCart.toolbarIcon.setImageResource(R.drawable.ic_cart)
        binding.toolbarCart.toolbarIcon.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.esewa_bg_light))

        binding.continueShoppingBtn.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        setupRecommendedRecyclerView()
        observeData()

        binding.cartScrollLayer.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                val totalHeight = v.getChildAt(0).measuredHeight - v.measuredHeight
                if (scrollY >= totalHeight - 200) {
                    cartViewModel.loadMoreRecommended()
                }
            })
    }

    private fun initAdapters() {
        recommendedAdapter = createRecommendedProductAdapter()
    }

    private fun setupRecommendedRecyclerView() {
        val spanCount = getProductSpanCount()
        val layoutManager = GridLayoutManager(requireContext(), spanCount)

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (recommendedAdapter.isLoadingFooterShown() &&
                    position == recommendedAdapter.itemCount - 1) spanCount else 1
            }
        }

        binding.rvRecommendedCart.layoutManager = layoutManager
        binding.rvRecommendedCart.adapter = recommendedAdapter
        binding.rvRecommendedCart.isNestedScrollingEnabled = false
    }

    private fun observeData() {
        cartViewModel.recommendedProducts.observe(viewLifecycleOwner) { recommended ->
            recommendedAdapter.submitFullList(recommended)
        }

        cartViewModel.recommendedLoading.observe(viewLifecycleOwner) { isLoading ->
            recommendedAdapter.setLoading(isLoading)
        }
    }

    private fun getProductSpanCount(): Int {
        return when {
            resources.configuration.screenWidthDp >= 840 -> 4
            resources.configuration.screenWidthDp >= 600 -> 3
            else -> 2
        }
    }

    private fun getItemsToShow(rows: Int): Int {
        return rows * getProductSpanCount()
    }

    private fun createRecommendedProductAdapter(): RecommendedProductAdapter{
        return RecommendedProductAdapter(
            viewModel = productCountViewModel,
            onClick = { product ->
                val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                intent.putExtra("product_id", product.id.toInt())
                startActivity(intent)
            },
            onAddToCartClick = { productId ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()){
                        productCountViewModel.addToCart(productId)
                    }else{
                        val coordinator = requireActivity().findViewById<View>(R.id.main)
                        val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                        SnackBarUtil.show(
                            view = coordinator,
                            context = requireContext(),
                            text = "Login to continue.",
                            anchorView = bottomNav,
                            actionText = "GO TO LOGIN"
                        ) {
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            },
            onRemoveOneFromCartClick = { productId ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if (authNavigator.isLoggedIn()){
                        productCountViewModel.removeOneFromCart(productId)
                    }
                }
            },
            onFavouriteClick = { productId ->
                viewLifecycleOwner.lifecycleScope.launch {
                    if(authNavigator.isLoggedIn()){
                        val isFav = productCountViewModel.isFavourite(productId).first()

                        if (isFav)
                            productCountViewModel.removeFromFavourites(productId)
                        else
                            productCountViewModel.addToFavourites(productId)
                    }
                    else{
                        val coordinator = requireActivity().findViewById<View>(R.id.main)
                        val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                        SnackBarUtil.show(
                            view = coordinator,
                            context = requireContext(),
                            text = "Login to continue.",
                            anchorView = bottomNav,
                            actionText = "GO TO LOGIN"
                        ) {
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        )
    }
}