package happy.kiki.happic.module.dailyhappic.ui.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DailyHappicTabViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int) = when (position) {
        0 -> DailyHappicPhotoFragment()
        1 -> DailyHappicTagFragment()
        else -> throw RuntimeException()
    }
}