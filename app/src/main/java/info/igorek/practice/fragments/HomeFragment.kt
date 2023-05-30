package info.igorek.practice.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.kirich1409.viewbindingdelegate.viewBinding
import info.igorek.practice.R
import info.igorek.practice.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = binding.pager
        viewPager.adapter = LessonsPagerAdapter(this)
    }

    private inner class LessonsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragmentList = listOf(
            HelloFragment(),
            TextFragment(),
            ListFragment(),
        )

        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

    }
}