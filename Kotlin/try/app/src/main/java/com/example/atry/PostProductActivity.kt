package com.example.atry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.atry.databinding.ActivityPostProductBinding
import com.example.atry.ui.fragments.PostProductFragment

class PostProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPostProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(PostProductFragment())

        binding.postProductBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.postProductFrame, fragment)
            .commit()
    }
}