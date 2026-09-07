package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class SecondFragment : Fragment(R.layout.fragment_second) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedText = view.findViewById<TextView>(R.id.selected_text)

        val buttonText = arguments?.getString("button_text")

        selectedText.text = buttonText
    }
}