package info.igorek.practice.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import info.igorek.practice.fragments.HelloFragment
import info.igorek.practice.fragments.ListFragment
import info.igorek.practice.fragments.TextFragment

class LessonsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

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