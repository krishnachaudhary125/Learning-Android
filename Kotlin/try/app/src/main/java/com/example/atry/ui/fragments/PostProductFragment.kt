package com.example.atry.ui.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.atry.databinding.FragmentPostProductBinding

class PostProductFragment : Fragment() {
    private lateinit var binding: FragmentPostProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostProductBinding.inflate(inflater, container, false)
        return binding.root
    }
}