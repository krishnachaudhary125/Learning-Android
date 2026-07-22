package com.example.atry

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.example.atry.ui.fragments.CartFragment
import com.example.atry.ui.fragments.FavouriteFragment
import com.example.atry.ui.fragments.HomeFragment
import com.example.atry.ui.fragments.MoreFragment
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {

    var selectedTab = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        val shop: LinearLayout? = findViewById(R.id.shopButton)
        val cart: LinearLayout? = findViewById(R.id.cartButton)
        val favourite: LinearLayout? = findViewById(R.id.favouriteButton)
        val more: LinearLayout? = findViewById(R.id.moreButon)

        val shopIcon: ImageView? = findViewById(R.id.shopIcon)
        val cartIcon: ImageView? = findViewById(R.id.cartIcon)
        val favouriteIcon: ImageView? = findViewById(R.id.favouriteIcon)
        val moreIcon: ImageView? = findViewById(R.id.moreIcon)

        val shopLabel: TextView? = findViewById(R.id.shopLabel)
        val cartLabel: TextView? = findViewById(R.id.cartLabel)
        val favouriteLabel: TextView? = findViewById(R.id.favouriteLabel)
        val moreLabel: TextView? = findViewById(R.id.moreLabel)

        if(intent.getBooleanExtra("openHome", false)){
            loadFragment(HomeFragment())
        }

        shop?.setOnClickListener {
            if (selectedTab != 1) {
                loadFragment(HomeFragment())

                onSelect(shop, shopLabel, shopIcon)
                onDeselect(cart, cartLabel, cartIcon)
                onDeselect(favourite, favouriteLabel, favouriteIcon)
                onDeselect(more, moreLabel, moreIcon)

                selectedTab = 1
            }
        }

        cart?.setOnClickListener {
            if (selectedTab != 2) {
                loadFragment(CartFragment())

                onSelect(cart, cartLabel, cartIcon)
                onDeselect(shop, shopLabel, shopIcon)
                onDeselect(favourite, favouriteLabel, favouriteIcon)
                onDeselect(more, moreLabel, moreIcon)

                selectedTab = 2
            }
        }

        favourite?.setOnClickListener {
            if (selectedTab != 3) {
                loadFragment(FavouriteFragment())

                onSelect(favourite, favouriteLabel, favouriteIcon)
                onDeselect(shop, shopLabel, shopIcon)
                onDeselect(cart, cartLabel, cartIcon)
                onDeselect(more, moreLabel, moreIcon)

                selectedTab = 3
            }
        }

        more?.setOnClickListener {
            if (selectedTab != 4) {
                loadFragment(MoreFragment())

                onSelect(more, moreLabel, moreIcon)
                onDeselect(shop, shopLabel, shopIcon)
                onDeselect(cart, cartLabel, cartIcon)
                onDeselect(favourite, favouriteLabel, favouriteIcon)

                selectedTab = 4
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrame, fragment)
            .commit()
    }


    private fun onSelect(linearLayout: LinearLayout?, textView: TextView?, imageView: ImageView?) {
        textView?.visibility = View.VISIBLE
        imageView?.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.green))
        linearLayout?.setBackgroundResource(R.drawable.navigation_background)
        linearLayout?.animate()
            ?.scaleX(1.1f)
            ?.scaleY(1.1f)
            ?.setDuration(100)
            ?.withEndAction {
                linearLayout?.animate()
                    ?.scaleX(1f)
                    ?.scaleY(1f)
                    ?.setDuration(100)
            }
    }

    private fun onDeselect(
        linearLayout: LinearLayout?,
        textView: TextView?,
        imageView: ImageView?
    ) {
        textView?.visibility = View.GONE
        imageView?.imageTintList =
            ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black))
        linearLayout?.setBackgroundResource(android.R.color.transparent)
    }
}