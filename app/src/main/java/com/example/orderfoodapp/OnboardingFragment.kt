package com.example.orderfoodapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

class OnboardingFragment : Fragment() {

    companion object {
        fun newInstance(imageResId: Int, title: String) = OnboardingFragment().apply {
            arguments = Bundle().apply {
                putInt("imageResId", imageResId)
                putString("title", title)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView: ImageView = view.findViewById(R.id.onboarding_img)
        val textView: TextView = view.findViewById(R.id.onboarding_text)

        arguments?.let {
            imageView.setImageResource(it.getInt("imageResId"))
            textView.text = it.getString("title")
        }
    }
}