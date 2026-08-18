package com.example.eSewaMarket.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.eSewaMarket.R
import com.example.eSewaMarket.ui.compose.FavouriteFragmentScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

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
                var checked by remember { mutableStateOf(false) }

                val products by favouriteViewModel.favouriteProducts()
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList()
                    )

                FavouriteFragmentScreen(
                    products = products,
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    },
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
                    deleteAll = {
                        deleteAllAlertDialog{
                            checked = false
                        }
                    },
                    onProductClick = {},
                    onAddToCartClick = {},
                    onOptionClick = {},
                    onTickClick = {}
                )
            }
        }
    }

    private fun deleteAllAlertDialog(onComplete: () -> Unit) {

        val titleView = TextView(requireContext()).apply {
            text = "Do you want to delete all favourite products?"
            textSize = 18f
            setTextColor(ContextCompat.getColor(context, R.color.text_dark))
            setPadding(60, 60, 0, 0)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setCustomTitle(titleView)
            .setNegativeButton("No", null)
            .setPositiveButton("Yes"){ _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    favouriteViewModel.deleteAllFavourites()
                    onComplete()
                }
            }
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(),R.color.green))

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }
}