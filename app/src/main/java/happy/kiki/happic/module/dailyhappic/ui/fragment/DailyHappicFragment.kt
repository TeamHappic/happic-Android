package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import happy.kiki.happic.databinding.FragmentDailyHappicBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.pushActivity
import happy.kiki.happic.module.core.util.extension.showToast
import happy.kiki.happic.module.upload.ui.activity.UploadHappicActivity
import happy.kiki.happic.module.upload.ui.activity.UploadHappicActivity.Argument

class DailyHappicFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicBinding>()
    private lateinit var dailyHappicTabViewPagerAdapter: DailyHappicTabViewPagerAdapter
    private val vm by viewModels<DailyHappicViewModel>()

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
        dailyHappicTabViewPagerAdapter = DailyHappicTabViewPagerAdapter(
            requireActivity().supportFragmentManager.fragmentFactory, this@DailyHappicFragment
        )
        binding.vpDailyHappic.adapter = dailyHappicTabViewPagerAdapter
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.run {
            pushActivity<UploadHappicActivity>(Argument(this))
        }
    }

    private fun configureNavigation() {
        collectFlowWhenStarted(vm.isTodayUploadedApi.data) {
            it?.run {
                binding.ivAddImage.setOnClickListener {
                    if (isPosted) showToast("하루해픽은 1일 1회 등록만 가능합니다.")
                    else launcher.launch("image/*")
                }
            }
        }
    }
}