package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class FirstFragment : Fragment(R.layout.fragment_first) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button1 = view.findViewById<Button>(R.id.button1)
        val button2 = view.findViewById<Button>(R.id.button2)
        val button3 = view.findViewById<Button>(R.id.button3)
        val button4 = view.findViewById<Button>(R.id.button4)
        val button5 = view.findViewById<Button>(R.id.button5)

        button1.setOnClickListener {
            openSecondFragment(button1.text.toString())
        }

        button2.setOnClickListener {
            openSecondFragment(button2.text.toString())
        }

        button3.setOnClickListener {
            openSecondFragment(button3.text.toString())
        }

        button4.setOnClickListener {
            openSecondFragment(button4.text.toString())
        }

        button5.setOnClickListener {
            openSecondFragment(button5.text.toString())
        }
    }

    private fun openSecondFragment(buttonText: String) {

        val secondFragment = SecondFragment()

        val bundle = Bundle()
        bundle.putString("button_text", buttonText)

        secondFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, secondFragment)
            .addToBackStack(null)
            .commit()
    }
}