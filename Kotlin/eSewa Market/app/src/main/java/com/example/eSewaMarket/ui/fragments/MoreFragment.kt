package com.example.eSewaMarket.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.eSewaMarket.FaqActivity
import com.example.eSewaMarket.LoginActivity
import com.example.eSewaMarket.MainActivity
import com.example.eSewaMarket.MyReturnActivity
import com.example.eSewaMarket.R
import com.example.eSewaMarket.RegisterActivity
import com.example.eSewaMarket.ShippingAddressActivity
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.FragmentMoreBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding
    private lateinit var userSessionRepository: UserSessionRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        userSessionRepository = UserSessionRepository(requireContext())
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbarMore.toolbarBackTitleAction) { view, insets ->

            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            view.layoutParams.height =
                view.context.resources.getDimensionPixelSize(
                    com.google.android.material.R.dimen.mtrl_toolbar_default_height
                ) + top

            view.requestLayout()

            insets
        }

        binding.toolbarMore.toolbarTitle.text = "More"
        binding.toolbarMore.toolbarIcon.setImageResource(R.drawable.ic_more_vertical)

        binding.login.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.register.setOnClickListener {
            val intent = Intent(requireContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.faq.setOnClickListener {
            val intent = Intent(requireContext(), FaqActivity::class.java)
            startActivity(intent)
        }

        binding.myReturn.setOnClickListener {
            val intent = Intent(requireContext(), MyReturnActivity::class.java)
            startActivity(intent)
        }

        binding.shippingAddress.setOnClickListener {
            val intent = Intent(requireContext(), ShippingAddressActivity::class.java)
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userSessionRepository.isLoggedIn.collect { isLoggedIn ->

                if (isLoggedIn) {
                    binding.userLogin.visibility = View.GONE
                    binding.profileLayout.visibility = View.VISIBLE
                    binding.underline.visibility = View.VISIBLE
                    binding.myProfileAndMyOrder.visibility = View.VISIBLE
                    binding.logoutBtn.visibility = View.VISIBLE
                } else {
                    binding.userLogin.visibility = View.VISIBLE
                    binding.profileLayout.visibility = View.GONE
                    binding.underline.visibility = View.GONE
                    binding.myProfileAndMyOrder.visibility = View.GONE
                    binding.logoutBtn.visibility = View.GONE
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            userSessionRepository.user.collect { user ->

                binding.userName.text = user.name
                binding.userPhone.text = user.phone

                if (!user.photoUrl.isNullOrEmpty()) {
                    Glide.with(requireContext())
                        .load(user.photoUrl)
                        .placeholder(R.drawable.profile_image)
                        .error(R.drawable.profile_image)
                        .into(binding.userProfile)
                } else {
                    binding.userProfile.setImageResource(R.drawable.profile_image)
                }
            }
        }

        binding.logoutBtn.setOnClickListener {

            binding.loadingOverlay.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.logoutBtn.isEnabled = false

            FirebaseAuth.getInstance().signOut()

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    userSessionRepository.logout()

                    val intent = Intent(requireContext(), MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                        putExtra("login_success", true)
                    }
                    startActivity(intent)
                    requireActivity().finish()
                } finally {
                    binding.loadingOverlay.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.logoutBtn.isEnabled = true
                }
            }
        }

        return binding.root
    }
}