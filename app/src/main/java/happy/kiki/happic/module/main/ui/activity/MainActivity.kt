package happy.kiki.happic.module.main.ui.activity

import DailyHappicFragment
import HomeFragment
import ReportFragment
import SettingFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import happy.kiki.happic.databinding.ActivityMainBinding
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.replaceFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        attachFragment()
        configureBottomTab()
    }

    private fun attachFragment() {
        addFragment<HomeFragment>(binding.fragmentContainer, skipAddToBackStack = true)
    }

    private fun configureBottomTab() {
        binding.bottomTab.onTabSelectedListener = {
            when (it) {
                0 -> replaceFragment<HomeFragment>(binding.fragmentContainer, skipAddToBackStack = true)
                1 -> replaceFragment<DailyHappicFragment>(binding.fragmentContainer, skipAddToBackStack = true)
                2 -> replaceFragment<ReportFragment>(binding.fragmentContainer, skipAddToBackStack = true)
                3 -> replaceFragment<SettingFragment>(binding.fragmentContainer, skipAddToBackStack = true)
            }
        }
    }
}