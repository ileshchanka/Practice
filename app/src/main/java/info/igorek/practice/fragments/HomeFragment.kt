package info.igorek.practice.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import info.igorek.practice.R

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager = view.findViewById(R.id.pager)
        viewPager.adapter = LessonsPagerAdapter(this)
    }

    private inner class LessonsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragmentList = listOf(
            HelloFragment(),
            TextFragment(),
        )

        override fun getItemCount(): Int {
            return fragmentList.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragmentList[position]
        }

    }
}