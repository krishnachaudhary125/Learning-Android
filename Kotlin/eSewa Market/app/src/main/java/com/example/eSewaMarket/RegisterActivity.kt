package com.example.eSewaMarket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.eSewaMarket.data.models.UserSyncRequest
import com.example.eSewaMarket.databinding.ActivityRegisterBinding
import com.example.eSewaMarket.ui.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.enableEdgeToEdge(window)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(binding.esewaLogo) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

            view.setPadding(
                view.paddingLeft,
                top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        binding.redirectToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        userViewModel.loading.observe(this) { isLoading ->
            binding.loadingOverlay.visibility =
                if (isLoading) View.VISIBLE else View.GONE
        }

        userViewModel.user.observe(this) {

            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        userViewModel.error.observe(this) { error ->
            binding.loadingOverlay.visibility = View.GONE

            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        binding.cbTerms.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.uncheckedTermMsg.visibility = View.GONE
            }
        }

        binding.registerBtn.setOnClickListener {

            val fullName = binding.fname.text.toString().trim()
            val phone = binding.phone.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            when {

                fullName.isEmpty() -> {
                    binding.fname.error = "Full name is required"
                    binding.fname.requestFocus()
                    return@setOnClickListener
                }

                phone.isEmpty() -> {
                    binding.phone.error = "Phone number is required"
                    binding.phone.requestFocus()
                    return@setOnClickListener
                }

                !phone.matches(Regex("^(?:(\\+977[-.\\s]?)?9[78]\\d{8}|\\+(?!977)[1-9]\\d{6,14})$")) -> {
                    binding.phone.error = "Enter valid phone number"
                    binding.phone.requestFocus()
                    return@setOnClickListener
                }

                phone.length != 10 -> {
                    binding.phone.error = "Enter valid phone number"
                    binding.phone.requestFocus()
                    return@setOnClickListener
                }

                email.isEmpty() -> {
                    binding.email.error = "Email is required"
                    binding.email.requestFocus()
                    return@setOnClickListener
                }

                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.email.error = "Enter valid email"
                    binding.email.requestFocus()
                    return@setOnClickListener
                }

                password.isEmpty() -> {
                    binding.password.error = "Password is required"
                    binding.password.requestFocus()
                    return@setOnClickListener
                }

                password.length < 8 -> {
                    binding.password.error = "Password must be at least 8 characters"
                    binding.password.requestFocus()
                    return@setOnClickListener
                }

                !password.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")) -> {
                    binding.password.error = "Password must contain uppercase, lowercase, number & special character"
                    binding.password.requestFocus()
                    return@setOnClickListener
                }

                !binding.cbTerms.isChecked -> {
                    binding.uncheckedTermMsg.visibility = View.VISIBLE
                    binding.uncheckedTermMsg.error
                    binding.uncheckedTermMsg.text = "❗Please accept the terms and conditions"
                    binding.uncheckedTermMsg.requestFocus()
                    return@setOnClickListener
                }

                else -> {

                    binding.loadingOverlay.visibility = View.VISIBLE

                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                val firebaseUser = auth.currentUser

                                firebaseUser?.sendEmailVerification()
                                firebaseUser?.getIdToken(true)
                                    ?.addOnSuccessListener { result ->
                                        val token = result.token ?: return@addOnSuccessListener
                                        val request = UserSyncRequest(fullName = fullName, email = email, phone = phone)
                                        userViewModel.syncUser(token, request)
                                    }
                                    ?.addOnFailureListener { e ->
                                        binding.loadingOverlay.visibility = View.GONE
                                        Toast.makeText(this, e.localizedMessage, Toast.LENGTH_LONG).show()
                                    }

                            } else {
                                binding.loadingOverlay.visibility = View.GONE
                                Log.e("FirebaseRegister", "Registration failed", task.exception)
                                Toast.makeText(this, task.exception?.localizedMessage ?: "Registration failed", Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }
    }
}