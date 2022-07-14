package happy.kiki.happic.module.dailyhappic.ui.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.viewpager2.adapter.FragmentStateAdapter

class DailyHappicTabViewPagerAdapter(private val factory: FragmentFactory, private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        val classLoader = fragment.requireActivity().classLoader
        return when (position) {
            0 -> factory.instantiate(classLoader, DailyHappicPhotoFragment::class.java.name)
            1 -> factory.instantiate(classLoader, DailyHappicTagFragment::class.java.name)
            else -> throw RuntimeException()
        }
    }
}