package com.example.eSewaMarket.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.example.eSewaMarket.MainActivity
import com.example.eSewaMarket.ProductDetailActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.models.FavouriteResponse
import com.example.eSewaMarket.ui.compose.FavouriteFragmentScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.utils.SnackBarUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class FavouriteFragment : Fragment() {
    private val favouriteViewModel: FavouriteViewModel by viewModels {
        ViewModelFactoryProvider.favouriteFactory(requireContext())
    }
    private val cartViewModel: CartViewModel by viewModels {
        ViewModelFactoryProvider.cartFactory(requireContext())
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

                val cartCount by cartViewModel
                    .cartCount()
                    .collectAsStateWithLifecycle(initialValue = 0)

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
                    noOfItems = products.size,
                    cartCount = cartCount,
                    onCartClick = {
                        val intent = Intent(requireContext(), MainActivity::class.java).apply {
                            putExtra("open_fragment", "cart")
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        startActivity(intent)
                    },
                    deleteAll = {
                        deleteAllAlertDialog(products) {
                            checked = false
                        }
                    },
                    continueShopping = {
                        val intent = Intent(requireContext(), MainActivity::class.java).apply {
                            putExtra("open_fragment", "home")
                            flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        }
                        startActivity(intent)
                    },
                    onProductClick = { products ->
                        val intent = Intent(requireContext(), ProductDetailActivity::class.java)
                        intent.putExtra("product_id", products.productId)
                        startActivity(intent)
                    },
                    onAddToCartClick = { product ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            try {
                                cartViewModel.addToCartFromFavourite(product)
                                val coordinator = requireActivity().findViewById<View>(R.id.main)
                                val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                                SnackBarUtil.show(
                                    view = coordinator,
                                    context = requireContext(),
                                    text = "Added to cart successfully.",
                                    anchorView = bottomNav,
                                    actionText = "GO TO CART"
                                ) {
                                    val intent =
                                        Intent(requireContext(), MainActivity::class.java).apply {
                                            putExtra("open_fragment", "cart")
                                            flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                        }
                                    startActivity(intent)
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    requireContext(), "Failed to add product in cart.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                throw e
                            }
                        }
                    },
                    onOptionClick = {},
                    onTickClick = {},
                    onDeleteClick = { product ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            try {
                                favouriteViewModel.removeOne(product.productId)
                                val coordinator = requireActivity().findViewById<View>(R.id.main)
                                val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                                SnackBarUtil.show(
                                    view = coordinator,
                                    context = requireContext(),
                                    text = "(1) Item has been deleted.",
                                    anchorView = bottomNav,
                                    duration = 5000,
                                    actionText = "UNDO"
                                ) {
                                    viewLifecycleOwner.lifecycleScope.launch {
                                        favouriteViewModel.restoreFavourite(product)
                                    }
                                    SnackBarUtil.show(
                                        view = coordinator,
                                        context = requireContext(),
                                        text = "(1) Item restored successfully.",
                                        anchorView = bottomNav
                                    )
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    requireContext(),
                                    "Failed to delete product.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                throw e
                            }
                        }
                    }
                )
            }
        }
    }

    private fun deleteAllAlertDialog(
        products: List<FavouriteResponse>,
        onComplete: () -> Unit
    ) {

        val titleView = TextView(requireContext()).apply {
            text = getString(R.string.alert_dialog_delete)
            textSize = 18f
            setTextColor(ContextCompat.getColor(context, R.color.text_dark))
            setPadding(60, 60, 0, 0)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setCustomTitle(titleView)
            .setNegativeButton("No", null)
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val deletedProducts = products.toList()
                    favouriteViewModel.deleteAllFavourites()
                    val coordinator = requireActivity().findViewById<View>(R.id.main)
                    val bottomNav = requireActivity().findViewById<View>(R.id.bottomNav)

                    SnackBarUtil.show(
                        view = coordinator,
                        context = requireContext(),
                        text = "(${products.size}) Items has been deleted.",
                        anchorView = bottomNav,
                        duration = 5000,
                        actionText = "UNDO"
                    ) {
                        viewLifecycleOwner.lifecycleScope.launch {

                            deletedProducts.forEach { product ->
                                favouriteViewModel.restoreFavourite(product)
                            }

                            SnackBarUtil.show(
                                view = coordinator,
                                context = requireContext(),
                                text = "(${products.size}) Items restored successfully.",
                                anchorView = bottomNav
                            )
                        }
                    }
                    onComplete()
                }
            }
            .create()

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
    }
}