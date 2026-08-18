package com.example.eSewaMarket.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eSewaMarket.ui.compose.FavouriteFragmentScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel

class FavouriteFragment : Fragment() {
    private val favouriteViewModel: FavouriteViewModel by viewModels {
        ViewModelFactoryProvider.favouriteFactory(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val products by favouriteViewModel.favouriteProducts()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList()
                    )

                FavouriteFragmentScreen(
                    products = products,
                    onBackClick = {
                        requireActivity()
                            .onBackPressedDispatcher
                            .onBackPressed()
                    },
                    noOfItems = {
                        Text(
                            text = "( ${products.size} )"
                        )
                    },
                    deleteAll = {},
                    onProductClick = {},
                    onAddToCartClick = {},
                    onOptionClick = {},
                    onTickClick = {}
                )
            }
        }
    }
}