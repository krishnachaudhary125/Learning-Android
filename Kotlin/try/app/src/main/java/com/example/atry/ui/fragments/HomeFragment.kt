package com.example.atry.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.atry.FaqActivity
import com.example.atry.NotificationActivity
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
}