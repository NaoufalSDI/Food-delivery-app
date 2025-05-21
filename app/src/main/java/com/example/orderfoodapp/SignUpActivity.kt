package com.example.orderfoodapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.orderfoodapp.model.AuthManager
import com.example.orderfoodapp.utils.ValidationUtils

class SignUpActivity : AppCompatActivity() {
    private lateinit var authManager: AuthManager
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var signUpButton: Button
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        authManager = AuthManager()
        emailField = findViewById(R.id.emailSingup)
        passwordField = findViewById(R.id.passwordSignup)
        confirmPasswordField = findViewById(R.id.confirmPasswordSignup)
        signUpButton = findViewById(R.id.creerCompteButton)
        loginButton = findViewById(R.id.connecterCompteBtn)

        loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        signUpButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()
            val confirmPassword = confirmPasswordField.text.toString()

            if (!ValidationUtils.isValidEmail(email)) {
                emailField.error = "Email invalide"
                return@setOnClickListener
            }

            if (!ValidationUtils.isValidPassword(password)) {
                passwordField.error = "Minimum 6 caractères"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                confirmPasswordField.error = "Les mots de passe ne correspondent pas"
                return@setOnClickListener
            }

            authManager.signUpUser(email, password, {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            })
        }
    }
}