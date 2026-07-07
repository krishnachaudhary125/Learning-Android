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

        val user = findViewById<EditText>(R.id.id)
        val psw = findViewById<EditText>(R.id.psw)
        val check = findViewById<CheckBox>(R.id.remember)
        val forgot = findViewById<TextView>(R.id.forgot)
        val login = findViewById<Button>(R.id.btn)
        val register = findViewById<Button>(R.id.register)
        val userName: String = "krishna"
        var userPsw: String = "Krishna@123"

        login.setOnClickListener {
            val userText: String = user.text.toString()
            val pswText: String = psw.text.toString()
            if(userText.isEmpty()||pswText.isEmpty()){
                Toast.makeText(this, "Username and Password cannot be empty", Toast.LENGTH_SHORT).show()
            }
            else if(userText == userName && pswText == userPsw){
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("userName", userText)
                startActivity(intent)
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_SHORT).show()
            }
        }

        forgot.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.forgot_password, null)
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            val u = view.findViewById<EditText>(R.id.user)
            val btn = view.findViewById<Button>(R.id.btn)

            btn.setOnClickListener {
                val us: String = u.text.toString()
                if (us == userName){
                    dialog.dismiss()
                    val build = AlertDialog.Builder(this)
                    val v = layoutInflater.inflate(R.layout.forgot_psw_dialog, null)
                    build.setView(v)
                    val dia = build.create()
                    dia.show()

                    val nPsw = v.findViewById<EditText>(R.id.newPsw)
                    val cPsw = v.findViewById<EditText>(R.id.confirmPsw)
                    val submit = v.findViewById<Button>(R.id.submit)

                    submit.setOnClickListener {
                        val newPsw: String = nPsw.text.toString()
                        val conPsw: String = cPsw.text.toString()

                        if(newPsw.isEmpty() || conPsw.isEmpty()){
                            Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT).show()
                        } else if(newPsw == conPsw){
                            userPsw = newPsw
                            dia.dismiss()
                            Toast.makeText(this, "Password change successfully", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this, "Password not matched", Toast.LENGTH_SHORT).show()
                        }
                    }

                }else{
                    Toast.makeText(this, "Incorrect username", Toast.LENGTH_SHORT).show()
                }
            }

        }

        register.setOnClickListener {
            Toast.makeText(this, "Register here", Toast.LENGTH_SHORT).show()
        }
    }
}