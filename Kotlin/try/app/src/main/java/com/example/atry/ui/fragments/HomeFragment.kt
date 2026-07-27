package com.example.atry.ui.fragments

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.atry.NotificationActivity
import com.example.atry.PostProductActivity
import com.example.atry.ProductDetailActivity
import com.example.atry.R
import com.example.atry.databinding.FragmentHomeBinding
import com.example.atry.ui.adapters.BannerPagerAdapter
import com.example.atry.ui.adapters.CategoryAdapter
import com.example.atry.ui.adapters.HotDealCategoryAdapter
import com.example.atry.ui.adapters.ProductAdapter
import com.example.atry.ui.viewmodel.HomeViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var bannerAdapter: BannerPagerAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var hotDealCategoryAdapter: HotDealCategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var featuredProductAdapter: ProductAdapter
    private lateinit var hotDealProductAdapter: ProductAdapter
    private lateinit var recommendedAdapter: ProductAdapter

    private var recommendedCache: List<com.example.atry.data.models.Product>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.homeAppBar.appBar) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        setupBannerViewPager()
        setupCategoryRecyclerView()
        setupHotDealCategoryRecyclerView()
        setupPopularBrandRecyclerView()
        setupFeaturedProductRecyclerView()
        setupHotDealProductRecyclerView()
        setupRecommendedRecyclerView()
        observeData()

        Glide.with(this)
            .asGif()
            .load(R.drawable.loading)
            .into(binding.loadingGif)

        binding.homeAppBar.notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.vpBanner.adapter = bannerAdapter

        TabLayoutMediator(binding.tabLayoutIndicator, binding.vpBanner){_,_ -> }.attach()

        var animator: ValueAnimator? = null
        var btnExpanded = true
        binding.shopScrollLayer.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if (animator == null) {
                    animator = createAnimator()
                }

                if (scrollY > oldScrollY && btnExpanded) {
                    animator.start()
                    btnExpanded = !btnExpanded
                } else if (scrollY < oldScrollY && !btnExpanded) {
                    animator.reverse()
                    btnExpanded = !btnExpanded
                }

                val totalHeight = v.getChildAt(0).measuredHeight - v.measuredHeight
                if (scrollY >= totalHeight) {

                }
            }
        )

        binding.floatingSellButton.setOnClickListener {
            val intent = Intent(requireContext(), PostProductActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initAdapters() {
        bannerAdapter = BannerPagerAdapter { banner ->
            Toast.makeText(requireContext(), "Banner: ", Toast.LENGTH_SHORT).show()
        }

        categoryAdapter = CategoryAdapter { category ->
            Toast.makeText(requireContext(), "Category: ", Toast.LENGTH_SHORT).show()
        }

        hotDealCategoryAdapter = HotDealCategoryAdapter { hotDealCategories ->
            Toast.makeText(requireContext(), "Category: ", Toast.LENGTH_SHORT).show()
        }

        productAdapter = ProductAdapter { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        featuredProductAdapter = ProductAdapter { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        hotDealProductAdapter = ProductAdapter { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }

        recommendedAdapter = ProductAdapter { product ->
            val intent = Intent(requireContext(), ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }
    }

    private fun setupBannerViewPager() {
        binding.vpBanner.apply {
            adapter = bannerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            isUserInputEnabled = true
        }
    }

    private fun setupCategoryRecyclerView() {
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupHotDealCategoryRecyclerView() {
        binding.hotDealCategoriesRecyclerView.apply {
            layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                flexWrap = FlexWrap.WRAP
            }
            adapter = hotDealCategoryAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupPopularBrandRecyclerView() {
        binding.rvPopularBrand.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPopularBrand.adapter = productAdapter
    }

    private fun setupFeaturedProductRecyclerView() {
        binding.rvFeaturedProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFeaturedProduct.adapter = featuredProductAdapter
    }

    private fun setupHotDealProductRecyclerView() {
        binding.rvHotDealProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvHotDealProduct.adapter = hotDealProductAdapter
    }

    private fun setupRecommendedRecyclerView() {
        binding.rvRecommended.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvRecommended.adapter = recommendedAdapter
    }

    private fun observeData() {
        homeViewModel.banners.observe(viewLifecycleOwner) { banners ->
            bannerAdapter.submitList(banners)
        }

        homeViewModel.category.observe(viewLifecycleOwner) { category ->
            categoryAdapter.submitList(category)
        }

        homeViewModel.hotDealCategories.observe(viewLifecycleOwner) { hotDealCategories ->
            hotDealCategoryAdapter.submitList(hotDealCategories)
        }

        homeViewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products.drop(22).take(4))
            featuredProductAdapter.submitList(products.take(2))
            hotDealProductAdapter.submitList(products.drop(2).take(2))

            val recommended = recommendedCache ?: products.shuffled().take(8).also { recommendedCache = it }
            recommendedAdapter.submitList(recommended)
        }
    }

    private fun createAnimator(): ValueAnimator {
        val initSize = binding.floatingSell.measuredWidth
        val animator = ValueAnimator.ofInt(initSize, 0)
        animator.duration = 250

        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val layoutParams = binding.floatingSell.layoutParams
            layoutParams.width = value
            binding.floatingSell.requestLayout()
        }
        return animator
    }
}