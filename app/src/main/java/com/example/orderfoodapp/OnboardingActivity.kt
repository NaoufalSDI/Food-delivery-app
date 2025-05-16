package com.example.orderfoodapp

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: OnboardingAdapter
    private lateinit var passerNext: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_onboarding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewPager = findViewById(R.id.viewPager)
        passerNext = findViewById(R.id.passerNext)
        val fragments = listOf(
            OnboardingFragment.newInstance(R.drawable.onboarding_first_img, "La discription du premier fragment d'onboarding"),
            OnboardingFragment.newInstance(R.drawable.onboarding_first_img, "La discription du deuxieme fragment d'onboarding"),
        )

        adapter = OnboardingAdapter(this, fragments)
        viewPager.adapter = adapter

        passerNext.setOnClickListener {

            if (viewPager.currentItem < fragments.size - 1) {
                viewPager.currentItem += 1
            } else {

            }
        }
    }


}