package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.eSewaMarket.data.api.RetrofitInstance
import com.example.eSewaMarket.data.repository.CartRepository
import com.example.eSewaMarket.data.repository.FavouriteRepository
import com.example.eSewaMarket.data.repository.UserSessionRepository
import com.example.eSewaMarket.databinding.ActivityLoginBinding
import com.example.eSewaMarket.ui.factory.CartViewModelFactory
import com.example.eSewaMarket.ui.factory.FavouriteViewModelFactory
import com.example.eSewaMarket.ui.viewmodel.CartViewModel
import com.example.eSewaMarket.ui.viewmodel.FavouriteViewModel
import com.example.eSewaMarket.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var userViewModel: UserViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel

    private lateinit var userSessionRepository: UserSessionRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        userSessionRepository = UserSessionRepository(this)

        val database =
            (application as EsewaMarketApplication).database

        val cartRepository = CartRepository(
            cartDao = database.cartDao(),
            productDao = database.productDao(),
            userRepository = userSessionRepository,
            apiService = RetrofitInstance.api
        )

        val favouriteRepository = FavouriteRepository(
            favouriteDao = database.favouriteDao(),
            productDao = database.productDao(),
            userRepository = userSessionRepository,
            apiService = RetrofitInstance.api
        )

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        cartViewModel = ViewModelProvider(
            this,
            CartViewModelFactory(cartRepository)
        )[CartViewModel::class.java]

        favouriteViewModel = ViewModelProvider(
            this,
            FavouriteViewModelFactory(favouriteRepository)
        )[FavouriteViewModel::class.java]

        observeViewModels()

        setupWindowInsets()

        setupClickListeners()
    }

    private fun observeViewModels() {

        userViewModel.loading.observe(this) { isLoading ->

            if (isLoading) {
                binding.loadingOverlay.visibility = View.VISIBLE
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.loadingOverlay.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        }

        userViewModel.user.observe(this) {

            cartViewModel.syncCartWithServer()
            favouriteViewModel.syncFavouritesWithServer()

            val intent = Intent(
                this@LoginActivity,
                MainActivity::class.java
            )

            intent.putExtra("login_success", true)

            startActivity(intent)
            finish()
        }

        userViewModel.error.observe(this) { error ->

            if (!error.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun setupWindowInsets() {

        ViewCompat.setOnApplyWindowInsetsListener(
            binding.esewaLogo
        ) { view, insets ->

            val top = insets.getInsets(
                WindowInsetsCompat.Type.statusBars()
            ).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }
    }

    private fun setupClickListeners() {

        binding.redirectToRegister.setOnClickListener {

            val intent = Intent(
                this,
                RegisterActivity::class.java
            )

            startActivity(intent)
        }

        binding.loginBtn.setOnClickListener {

            val email =
                binding.loginEmail.text.toString().trim()

            val password =
                binding.password.text.toString().trim()

            when {

                email.isEmpty() -> {
                    binding.loginEmail.error =
                        "Email is required"

                    binding.loginEmail.requestFocus()

                    return@setOnClickListener
                }

                password.isEmpty() -> {
                    binding.password.error =
                        "Password is required"

                    binding.password.requestFocus()

                    return@setOnClickListener
                }
            }

            binding.loadingOverlay.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            binding.loginBtn.isEnabled = false

            loginUser(email, password)
        }
    }

    private fun loginUser(
        email: String,
        password: String
    ) {

        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                getFirebaseToken()

            } else {

                binding.loadingOverlay.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.loginBtn.isEnabled = true

                Toast.makeText(
                    this,
                    task.exception?.message
                        ?: "Login failed",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun getFirebaseToken() {

        auth.currentUser
            ?.getIdToken(true)
            ?.addOnSuccessListener { result ->

                result.token?.let { token ->

                    userViewModel.getCurrentUser(token)
                }
            }
    }
}