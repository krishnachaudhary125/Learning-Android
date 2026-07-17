package com.example.atry.ui.fragments

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.atry.FaqActivity
import com.example.atry.NotificationActivity
import com.example.atry.PostProductActivity
import com.example.atry.R
import com.example.atry.databinding.FragmentHomeBinding
import com.example.atry.ui.adapters.BannerAdapter
import com.example.atry.ui.adapters.CategoryAdapter
import com.example.atry.ui.adapters.FeaturedProductAdapter
import com.example.atry.ui.adapters.HotDealCategoryAdapter
import com.example.atry.ui.adapters.HotDealProductAdapter
import com.example.atry.ui.adapters.ProductAdapter
import com.example.atry.ui.adapters.RecommendedAdapter
import com.example.atry.ui.viewmodel.HomeViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var hotDealCategoryAdapter: HotDealCategoryAdapter
    private lateinit var productAdapter: ProductAdapter
    private lateinit var featuredProductAdapter: FeaturedProductAdapter
    private lateinit var hotDealProductAdapter: HotDealProductAdapter
    private lateinit var recommendedAdapter: RecommendedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapters()
        setupBannerRecyclerView()
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

        binding.notification.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.bannerRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.bannerRecyclerView.adapter = bannerAdapter

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.bannerRecyclerView)

        repeat(3) {
            binding.tabLayoutIndicator.addTab(binding.tabLayoutIndicator.newTab())
        }

        binding.bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(recyclerView.layoutManager)
                    centerView?.let { view ->
                        val position = recyclerView.layoutManager?.getPosition(view) ?: 0
                        binding.tabLayoutIndicator.selectTab(binding.tabLayoutIndicator.getTabAt(position))
                    }
                }
            }
        })

        var animator : ValueAnimator? = null
        var btnExpanded = true
        binding.shopScrollLayer.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, oldScrollY ->
                if(animator == null){
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
        bannerAdapter = BannerAdapter { banner ->
            Toast.makeText(
                requireContext(),
                "Banner: ",
                Toast.LENGTH_SHORT
            ).show()
        }

        categoryAdapter = CategoryAdapter { category ->
            Toast.makeText(
                requireContext(),
                "Category: ",
                Toast.LENGTH_SHORT
            ).show()
        }

        hotDealCategoryAdapter = HotDealCategoryAdapter { hotDealCategories ->
            Toast.makeText(
                requireContext(),
                "Category: ",
                Toast.LENGTH_SHORT
            ).show()
        }

        productAdapter = ProductAdapter { product ->
            Toast.makeText(
                requireContext(),
                product.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        featuredProductAdapter = FeaturedProductAdapter { product ->
            Toast.makeText(
                requireContext(),
                product.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        hotDealProductAdapter = HotDealProductAdapter { product ->
            Toast.makeText(
                requireContext(),
                product.title,
                Toast.LENGTH_SHORT
            ).show()
        }

        recommendedAdapter = RecommendedAdapter{ product ->
            Toast.makeText(requireContext(),
                product.title,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun setupBannerRecyclerView() {
        binding.bannerRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = bannerAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupCategoryRecyclerView() {
        binding.categoryRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = categoryAdapter
            isNestedScrollingEnabled = false
        }
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
            productAdapter.submitList(products.take(4))
        }

        homeViewModel.products.observe(viewLifecycleOwner) { products ->
            featuredProductAdapter.submitList(products.take(2))
        }

        homeViewModel.products.observe(viewLifecycleOwner){ products ->
            hotDealProductAdapter.submitList(products.drop(2).take(2))
        }

        homeViewModel.products.observe(viewLifecycleOwner){ products ->
            recommendedAdapter.submitList(products.drop(4).take(8))
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

    private fun setupPopularBrandRecyclerView(){
        binding.rvPopularBrand.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPopularBrand.adapter = productAdapter

    }

    private fun setupFeaturedProductRecyclerView(){
        binding.rvFeaturedProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvFeaturedProduct.adapter = featuredProductAdapter

    }

    private fun setupHotDealProductRecyclerView(){
        binding.rvHotDealProduct.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvHotDealProduct.adapter = hotDealProductAdapter

    }

    private fun setupRecommendedRecyclerView(){
        binding.rvRecommended.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvRecommended.adapter = recommendedAdapter

    }

    private fun createAnimator(): ValueAnimator{
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