package com.example.eSewaMarket.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
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
import com.example.eSewaMarket.LoginActivity
import com.example.eSewaMarket.MainActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.ui.compose.FavouriteFragmentScreen
import com.example.eSewaMarket.ui.factory.ViewModelFactoryProvider
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.utils.AuthNavigator
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
    private lateinit var userSessionRepository: UserSessionRepository
    private lateinit var authNavigator: AuthNavigator
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userSessionRepository = UserSessionRepository(requireContext())
        authNavigator = AuthNavigator(userSessionRepository)
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
                    deleteAll = {
                        deleteAllAlertDialog {
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
                    onProductClick = {},
                    onAddToCartClick = {},
                    onOptionClick = {},
                    onTickClick = {},
                    onDeleteClick = { productId ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            if (authNavigator.isLoggedIn()) {
                                favouriteViewModel.removeOne(productId)
                            } else {
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
    }

    private fun deleteAllAlertDialog(onComplete: () -> Unit) {

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
                    favouriteViewModel.deleteAllFavourites()
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