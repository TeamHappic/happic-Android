package happy.kiki.happic.module.main.ui.activity

import HomeFragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import happy.kiki.happic.module.dailyhappic.ui.fragment.DailyHappicContainerFragment
import happy.kiki.happic.module.report.ui.fragment.ReportContainerFragment
import happy.kiki.happic.module.setting.ui.fragment.SettingFragment

class MainAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int) = when (position) {
        0 -> HomeFragment()
        1 -> DailyHappicContainerFragment()
        2 -> ReportContainerFragment()
        3 -> SettingFragment()
        else -> throw IllegalArgumentException()
    }
}