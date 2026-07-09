package com.example.atry.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.atry.databinding.FragmentHomeBinding
import com.example.atry.ui.adapters.BannerAdapter
import com.example.atry.ui.adapters.CategoryAdapter
import com.example.atry.ui.viewmodels.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var categoryAdapter: CategoryAdapter

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
        observeData()
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
    }
}