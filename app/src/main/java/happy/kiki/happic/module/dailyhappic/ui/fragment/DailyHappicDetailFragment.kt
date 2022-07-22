package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.transition.MaterialElevationScale
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentDailyHappicDetailBinding
import happy.kiki.happic.module.core.util.AutoClearedValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.report.util.koFormat
import happy.kiki.happic.module.report.util.padZero
import happy.kiki.happic.module.setting.ui.dialog.CommonDialog
import happy.kiki.happic.module.setting.ui.dialog.CommonDialog.Argument
import kotlinx.coroutines.flow.combine
import kotlin.math.absoluteValue
import kotlin.math.min

class DailyHappicDetailFragment : Fragment(), CommonDialog.Listener {
    private var binding by AutoClearedValue<FragmentDailyHappicDetailBinding>()

    private val vm by activityViewModels<DailyHappicViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicDetailBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        enterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
        returnTransition = MaterialElevationScale(false).apply {
            duration = 300L
        }
        configureHeader()
        configurePager()
    }

    private fun configureHeader() {
        binding.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.delete.setOnClickListener {
            CommonDialog.newInstance(
                Argument(
                    R.drawable.hp_ic_alert,
                    "해픽 삭제",
                    "사진 삭제시 사진과 태그가 모두\n지워집니다. 또한 해당 내용은\n복구가 불가능합니다.\n삭제하시겠습니까?",
                    "취소",
                    "삭제하기"
                )
            ).show(childFragmentManager, null)
        }

        collectFlowWhenStarted(vm.onDeleteHappic.flow) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        collectFlowWhenStarted(vm.deleteHappic.isLoading) {
            binding.indicator.isVisible = it
        }
    }

    override fun onClickRight() {
        if (vm.detailDailyHappicItem.value != null) vm.deleteHappic.call(vm.detailDailyHappicItem.value!!.id)
    }

    private fun configurePager() {
        binding.pager.run {
            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
            val pageOffsetPx = resources.getDimensionPixelOffset(R.dimen.pagerOffset)

            updateLayoutParams {
                height = screenWidth - (pageOffsetPx + pageMarginPx) * 2
            }

            this.adapter = DailyHappicDetailAdapter().apply {
                submitList(vm.dailyHappicApi.data.value) {
                    val data = vm.dailyHappicApi.dataOnlySuccess.value
                    val index = vm.detailDailyHappicIndex.value
                    if (data != null && index >= 0 && data.size > index) {
                        binding.pager.setCurrentItem(index, true)
                    }
                }
            }
            setShowSideItems(pageMarginPx, pageOffsetPx)
            overScrollMode = ViewPager2.OVER_SCROLL_NEVER

            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    vm.detailDailyHappicIndex.value = position
                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    val offset = min(positionOffset, 1 - positionOffset)
                    binding.date.alpha = 1 - offset * 0.5f
                    binding.tag.alpha = 1 - offset * 0.5f
                    binding.date.scaleX = 1 + offset
                    binding.date.scaleY = 1 + offset
                    binding.tag.scaleX = 1 + offset
                    binding.tag.scaleY = 1 + offset
                }
            })
        }

        collectFlowWhenStarted(
            vm.dailyHappicApi.dataOnlySuccess.combine(
                vm.detailDailyHappicIndex, ::Pair
            )
        ) { (data, index) ->
            if (data != null && index >= 0 && data.size > index && index != binding.pager.currentItem) {
                binding.pager.setCurrentItem(index, false)
            }
        }

        collectFlowWhenStarted(vm.detailDailyHappicItem) {
            val (year, month) = vm.selectedYearMonth.value
            binding.date.text = it?.run { "$year.${month.padZero(2)}.${it.day.padZero(2)}" } ?: ""
            binding.tag.text = it?.run { "#${it.hour.koFormat} #${it.where} #${it.who} #${it.what}" } ?: ""
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