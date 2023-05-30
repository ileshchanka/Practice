package info.igorek.practice.fragments

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import info.igorek.practice.R

class TextFragment : Fragment(R.layout.fragment_text) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val editText = view.findViewById<EditText>(R.id.edittext)

        editText.doAfterTextChanged { text ->
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }
}