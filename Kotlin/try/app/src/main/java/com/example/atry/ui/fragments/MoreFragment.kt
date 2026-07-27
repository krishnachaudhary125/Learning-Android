package com.example.atry.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.atry.FaqActivity
import com.example.atry.MyReturnActivity
import com.example.atry.R
import com.example.atry.ShippingAddressActivity
import com.example.atry.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {
    private lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
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

        return binding.root
    }
}