package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentDailyHappicDetailBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.screenWidth
import kotlin.math.absoluteValue

class DailyHappicDetailFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicDetailBinding>()

    private val vm by activityViewModels<DailyHappicViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicDetailBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureHeader()
        configurePager()
    }

    private fun configureHeader() = binding.back.setOnClickListener {
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun configurePager() {
        binding.pager.run {
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
            val pageOffsetPx = resources.getDimensionPixelOffset(R.dimen.pagerOffset)

            updateLayoutParams {
                height = screenWidth - (pageOffsetPx + pageMarginPx) * 2
            }

            this.adapter = DailyHappicDetailAdapter().apply {
                submitList(vm.dailyHappicApi.data.value)
            }
            setShowSideItems(pageMarginPx, pageOffsetPx)
            overScrollMode = ViewPager2.OVER_SCROLL_NEVER

            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                }
            })
        }
    }
}

fun ViewPager2.setShowSideItems(pageMarginPx: Int, offsetPx: Int) {
    clipToPadding = false
    clipChildren = false
    offscreenPageLimit = 5

    setPageTransformer { page, position ->
        val offset = position * -(2 * offsetPx + pageMarginPx)
        page.translationX = offset

        val scale = 1 - position.absoluteValue * 0.14f
        page.scaleX = scale
        page.scaleY = scale

        page.alpha = (1 - position.absoluteValue + 0.5f)
    }
}