package com.example.eSewaMarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eSewaMarket.databinding.ActivityHotDealProductsBinding

class HotDealProductsActivity : AppCompatActivity(){

    private lateinit var binding: ActivityHotDealProductsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityHotDealProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.tbHotDealProducts.root) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.composeView){ view, insets ->
            val bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom

            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                bottom
            )
            insets
        }

        binding.tbHotDealProducts.backBtn.setOnClickListener {
            onBackPressedDispatcher
                .onBackPressed()
        }

        binding.tbHotDealProducts.toolbarTitle.text = "Hot Deals of The Day"
    }
}