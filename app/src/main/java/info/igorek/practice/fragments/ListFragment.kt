package info.igorek.practice.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import info.igorek.practice.R
import info.igorek.practice.adapters.MyRecyclerAdapter
import info.igorek.practice.adapters.MyRecyclerType.ImageItem
import info.igorek.practice.adapters.MyRecyclerType.TextItem
import info.igorek.practice.databinding.FragmentListBinding

class ListFragment : Fragment(R.layout.fragment_list) {

    companion object {
        val list = listOf(
            ImageItem(R.drawable.baseline_time_to_leave_24),
            ImageItem(R.drawable.baseline_ac_unit_24),
            ImageItem(R.drawable.baseline_memory_24),
            TextItem("Kotlin"),
            TextItem("Java"),
            TextItem("Swift"),
            TextItem("Objective-C"),
            TextItem("Python"),
            ImageItem(R.drawable.baseline_record_voice_over_24),
            ImageItem(R.drawable.baseline_pedal_bike_24),
        )
    }

    private val binding by viewBinding(FragmentListBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MyRecyclerAdapter { message ->
            showDialog(message)
        }
        adapter.submitList(list)
        binding.recyclerview.adapter = adapter
    }

    private fun showDialog(message: String) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("Info")
            setMessage(message)
            setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }
}