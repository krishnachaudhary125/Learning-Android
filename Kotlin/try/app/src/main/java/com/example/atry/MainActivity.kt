package com.example.atry

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.atry.ui.fragments.CartFragment
import com.example.atry.ui.fragments.FavouriteFragment
import com.example.atry.ui.fragments.HomeFragment
import com.example.atry.ui.fragments.MoreFragment

class MainActivity : AppCompatActivity() {

    var selectedTab = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null){
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

        shop?.setOnClickListener {
            if(selectedTab != 1){
                loadFragment(HomeFragment())

                cartLabel?.visibility = View.INVISIBLE
                favouriteLabel?.visibility = View.INVISIBLE
                moreLabel?.visibility = View.INVISIBLE

                cartIcon?.setImageResource(R.drawable.ic_cart)
                favouriteIcon?.setImageResource(R.drawable.ic_favourite)
                moreIcon?.setImageResource(R.drawable.ic_more)

                cart?.setBackgroundResource(android.R.color.transparent)
                favourite?.setBackgroundResource(android.R.color.transparent)
                more?.setBackgroundResource(android.R.color.transparent)

                shopLabel?.visibility = View.VISIBLE
                shopIcon?.setImageResource(R.drawable.ic_shop_selected)
                shop.setBackgroundResource(R.drawable.navigation_background)

                shop.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(100)
                    .withEndAction {
                        shop.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                    }

                selectedTab = 1
            }
        }

        cart?.setOnClickListener {
            if(selectedTab != 2){
                loadFragment(CartFragment())

                shopLabel?.visibility = View.INVISIBLE
                favouriteLabel?.visibility = View.INVISIBLE
                moreLabel?.visibility = View.INVISIBLE

                shopIcon?.setImageResource(R.drawable.ic_shop)
                favouriteIcon?.setImageResource(R.drawable.ic_favourite)
                moreIcon?.setImageResource(R.drawable.ic_more)

                shop?.setBackgroundResource(android.R.color.transparent)
                favourite?.setBackgroundResource(android.R.color.transparent)
                more?.setBackgroundResource(android.R.color.transparent)

                cartLabel?.visibility = View.VISIBLE
                cartIcon?.setImageResource(R.drawable.ic_cart_selected)
                cart.setBackgroundResource(R.drawable.navigation_background)

                cart.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(100)
                    .withEndAction {
                        cart.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                    }

                selectedTab = 2
            }
        }

        favourite?.setOnClickListener {
            if(selectedTab != 3){
                loadFragment(FavouriteFragment())

                shopLabel?.visibility = View.INVISIBLE
                cartLabel?.visibility = View.INVISIBLE
                moreLabel?.visibility = View.INVISIBLE

                shopIcon?.setImageResource(R.drawable.ic_shop)
                cartIcon?.setImageResource(R.drawable.ic_cart)
                moreIcon?.setImageResource(R.drawable.ic_more)

                shop?.setBackgroundResource(android.R.color.transparent)
                cart?.setBackgroundResource(android.R.color.transparent)
                more?.setBackgroundResource(android.R.color.transparent)

                favouriteLabel?.visibility = View.VISIBLE
                favouriteIcon?.setImageResource(R.drawable.ic_favourite_selected)
                favourite.setBackgroundResource(R.drawable.navigation_background)

                favourite.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(100)
                    .withEndAction {
                        favourite.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                    }

                selectedTab = 3
            }
        }

        more?.setOnClickListener {
            if(selectedTab != 4){
                loadFragment(MoreFragment())

                shopLabel?.visibility = View.INVISIBLE
                cartLabel?.visibility = View.INVISIBLE
                favouriteLabel?.visibility = View.INVISIBLE

                shopIcon?.setImageResource(R.drawable.ic_shop)
                cartIcon?.setImageResource(R.drawable.ic_cart)
                favouriteIcon?.setImageResource(R.drawable.ic_favourite)

                shop?.setBackgroundResource(android.R.color.transparent)
                cart?.setBackgroundResource(android.R.color.transparent)
                favourite?.setBackgroundResource(android.R.color.transparent)

                moreLabel?.visibility = View.VISIBLE
                moreIcon?.setImageResource(R.drawable.ic_more_selected)
                more.setBackgroundResource(R.drawable.navigation_background)

                more.animate()
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .setDuration(100)
                    .withEndAction {
                        more.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                    }

                selectedTab = 4
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrame, fragment)
            .commit()
    }
}