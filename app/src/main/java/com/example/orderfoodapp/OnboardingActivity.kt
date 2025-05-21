package com.example.orderfoodapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.orderfoodapp.model.GoogleAuthManager
import com.example.orderfoodapp.model.UserModel
import kotlinx.coroutines.launch

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    private lateinit var passerNext: TextView
    private val RC_GOOGLE_SIGN_IN = 1001
    private lateinit var googleAuthManager: GoogleAuthManager
    private lateinit var userModel: UserModel
    private lateinit var googleSignInButton: Button
    private lateinit var seConnecterButton: Button
    private lateinit var creerCompteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        googleAuthManager = GoogleAuthManager()
        userModel = UserModel()
        viewPager = findViewById(R.id.viewPager)
        passerNext = findViewById(R.id.passerNext)
        googleSignInButton = findViewById(R.id.googleAuthButton)
        seConnecterButton = findViewById(R.id.seConnecterButton)
        creerCompteButton = findViewById(R.id.creerCompteButtonn)

        val fragments = listOf(
            OnboardingFragment.newInstance(
                R.drawable.onboarding_first_img,
                "Préparez-vous à un festin visuel et gustatif avec notre menu interactif !"
            ),
            OnboardingFragment.newInstance(
                R.drawable.onboarding_second_img,
                "Bienvenue dans le monde délicieux SirFood, où chaque plat raconte une histoire"
            )
        )

        adapter = OnboardingAdapter(this, fragments)
        viewPager.adapter = adapter

        passerNext.setOnClickListener {
            if (viewPager.currentItem < fragments.size - 1) {
                viewPager.currentItem += 1
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }


        googleSignInButton.setOnClickListener {
            val googleClient = googleAuthManager.getGoogleClient(this)
            googleClient.signOut().addOnCompleteListener {
                val signInIntent = googleClient.signInIntent
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
            }
        }

        seConnecterButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
            }
        creerCompteButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val account = googleAuthManager.extractGoogleAccount(data)

            if (account != null) {
                lifecycleScope.launch {
                    val result = userModel.signInWithGoogle(this@OnboardingActivity, account)
                    if (result.isSuccess) {
                        val intent = Intent(this@OnboardingActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@OnboardingActivity,
                            "Erreur: ${result.exceptionOrNull()?.localizedMessage}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    this,
                    "Erreur de connexion Google. Voir logcat pour les détails.",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("OnboardingActivity", "Échec de la connexion Google : compte null")
            }
        }
    }


}
