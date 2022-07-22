package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayoutMediator
import happy.kiki.happic.databinding.FragmentDailyHappicBinding
import happy.kiki.happic.module.core.util.AutoClearedValue

class DailyHappicFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentDailyHappicBinding>()
    private lateinit var dailyHappicTabViewPagerAdapter: DailyHappicTabViewPagerAdapter
    private val vm by activityViewModels<DailyHappicViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        setTabLayout()
        configureNavigation()
    }

    private fun setTabLayout() {
        val tabLabel = listOf("사진", "태그")

        TabLayoutMediator(binding.tabDailyHappic, binding.vpDailyHappic) { tab, position ->
            tab.text = tabLabel[position]
        }.attach()
    }

    private fun initAdapter() {
        dailyHappicTabViewPagerAdapter = DailyHappicTabViewPagerAdapter(this@DailyHappicFragment)
        binding.vpDailyHappic.adapter = dailyHappicTabViewPagerAdapter
    }

    private fun configureNavigation() {
        binding.ivAddImage.setOnClickListener {
            vm.navigateUploadApi.call()
        }
    }

    override fun onResume() {
        super.onResume()
        vm.fetchDailyHappics()
    }
}