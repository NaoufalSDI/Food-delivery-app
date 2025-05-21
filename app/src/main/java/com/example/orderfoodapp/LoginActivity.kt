package com.example.orderfoodapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.orderfoodapp.model.AuthManager
import com.example.orderfoodapp.model.GoogleAuthManager
import com.example.orderfoodapp.model.UserModel
import com.example.orderfoodapp.utils.ValidationUtils
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val RC_GOOGLE_SIGN_IN = 1001
    private lateinit var googleAuthManager: GoogleAuthManager
    private lateinit var userModel: UserModel
    private lateinit var googleSignInButton: Button
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var forgetPasswordText: TextView
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        authManager = AuthManager()
        googleAuthManager = GoogleAuthManager()
        userModel = UserModel()
        emailField = findViewById(R.id.emailLogin)
        passwordField = findViewById(R.id.passwordLogin)
        loginButton = findViewById(R.id.seConnecterButton)
        signUpButton = findViewById(R.id.creerCompteButtonn)
        forgetPasswordText = findViewById(R.id.forgetPassword)
        googleSignInButton = findViewById(R.id.googleAuthButton)

        googleSignInButton.setOnClickListener {
            val googleClient = googleAuthManager.getGoogleClient(this)
            googleClient.signOut().addOnCompleteListener {
                val signInIntent = googleClient.signInIntent
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
            }
        }

        signUpButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        loginButton.setOnClickListener {
            val email = emailField.text.toString().trim()
            val password = passwordField.text.toString()

            if (!ValidationUtils.isValidEmail(email)) {
                emailField.error = "Email invalide"
                return@setOnClickListener
            }

            if (!ValidationUtils.isValidPassword(password)) {
                passwordField.error = "Mot de passe trop court"
                return@setOnClickListener
            }

            authManager.loginUser(email, password, {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            })
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val account = googleAuthManager.extractGoogleAccount(data)

            if (account != null) {
                lifecycleScope.launch {
                    val result = userModel.signInWithGoogle(this@LoginActivity, account)
                    result.onFailure { e ->
                        Toast.makeText(
                            this@LoginActivity,
                            "Erreur: ${e.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, "Erreur de connexion Google", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
