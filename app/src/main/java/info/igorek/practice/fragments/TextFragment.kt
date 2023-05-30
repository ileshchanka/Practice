package info.igorek.practice.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import info.igorek.practice.R
import info.igorek.practice.databinding.FragmentTextBinding

class TextFragment : Fragment(R.layout.fragment_text) {

    private val binding by viewBinding(FragmentTextBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.edittext.doAfterTextChanged { text ->
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }
}