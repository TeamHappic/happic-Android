package happy.kiki.happic.module.main.ui.activity

import DailyHappicFragment
import HomeFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import happy.kiki.happic.databinding.ActivityMainBinding
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.isFragmentExist
import happy.kiki.happic.module.report.ui.fragment.ReportFragment
import happy.kiki.happic.module.setting.ui.fragment.SettingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        addInitialFragment()
        configureBottomTab()
    }

    private fun addInitialFragment() = showFragment<HomeFragment>()

    private fun configureBottomTab() {
        binding.bottomTab.onTabSelectedListener = {
            when (it) {
                0 -> showFragment<HomeFragment>()
                1 -> showFragment<DailyHappicFragment>()
                2 -> showFragment<ReportFragment>()
                3 -> showFragment<SettingFragment>()
            }
        }
    }

    private inline fun <reified T : Fragment> showFragment() = supportFragmentManager.commit {
        val shouldBeAdded = !isFragmentExist<T>()
        supportFragmentManager.fragments.forEach {
            if (it is T) show(it)
            else hide(it)
        }
        if (shouldBeAdded) addFragment<T>(binding.fragmentContainer, tag = T::class.java.name)
    }
}