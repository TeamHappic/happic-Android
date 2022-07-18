package happy.kiki.happic.module.core.ui.widget.util

import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.Tab

abstract class OnTabSelectedListenerAdapter : OnTabSelectedListener {
    override fun onTabSelected(tab: Tab) {}

    override fun onTabUnselected(tab: Tab) {}

    override fun onTabReselected(tab: Tab) {}
}