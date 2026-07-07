package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val user: EditText = findViewById(R.id.id)
        val psw: EditText = findViewById(R.id.psw)
        val check: CheckBox = findViewById(R.id.remember)
        val forgot: TextView = findViewById(R.id.forgot)
        val login: Button = findViewById(R.id.btn)
        val register: Button = findViewById(R.id.register)
        var userName: String = "krishna"
        var userPsw: String = "Krishna@123"

        login.setOnClickListener {
            val userText: String = user.text.toString()
            val pswText: String = psw.text.toString()
            if(userText == userName && pswText == userPsw){
                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()
            }
        }

        forgot.setOnClickListener {

            Toast.makeText(this, "Forgot Password", Toast.LENGTH_SHORT).show()
        }

        register.setOnClickListener {
            Toast.makeText(this, "Register here", Toast.LENGTH_SHORT).show()
        }
    }
}