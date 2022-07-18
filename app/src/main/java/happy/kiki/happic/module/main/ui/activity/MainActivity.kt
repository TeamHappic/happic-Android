package happy.kiki.happic.module.main.ui.activity

import DailyHappicFragment
import HomeFragment
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import happy.kiki.happic.databinding.ActivityMainBinding
import happy.kiki.happic.module.core.util.extension.addFragment
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.isFragmentExist
import happy.kiki.happic.module.report.ui.fragment.ReportContainerFragment
import happy.kiki.happic.module.setting.ui.fragment.SettingFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        configureBottomTab()
    }

    private fun configureBottomTab() {
        binding.bottomTab.onTabSelectedListener = {
            vm.tabIndex.value = it
        }
        collectFlowWhenStarted(vm.tabIndex) {
            when (it) {
                0 -> showFragment<HomeFragment>()
                1 -> showFragment<DailyHappicFragment>()
                2 -> showFragment<ReportContainerFragment>()
                3 -> showFragment<SettingFragment>()
            }
            binding.bottomTab.selectedTabIndex = it
        }
    }

    private inline fun <reified T : Fragment> showFragment() = supportFragmentManager.commit {
        val shouldBeAdded = !isFragmentExist<T>()
        supportFragmentManager.fragments.forEach {
            if (it is T) show(it)
            else hide(it)
        }
        if (shouldBeAdded) addFragment<T>(
            binding.fragmentContainer, tag = T::class.java.name, skipAddToBackStack = true
        )
    }
}