package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.helper.widget.Flow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentDailyHappicPhotoBinding
import happy.kiki.happic.databinding.ItemDailyHappicPhotoBinding
import happy.kiki.happic.module.core.util.AutoClearedValue
import happy.kiki.happic.module.core.util.debugE
import happy.kiki.happic.module.core.util.emitEvent
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.injectViewId
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.yearMonthText
import kotlinx.coroutines.flow.drop
import java.time.LocalDate

class DailyHappicPhotoFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentDailyHappicPhotoBinding>()
    private val vm by activityViewModels<DailyHappicViewModel>()
    private val navigationVm by activityViewModels<DailyHappicNavigationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicPhotoBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = vm
        setCards()
        configureMonthSelect()
    }

    private fun configureMonthSelect() = binding.monthSelect.apply {
        binding.borderMonth.setOnClickListener {
            vm.isMonthSelectOpened.value = !vm.isMonthSelectOpened.value
        }
        collectFlowWhenStarted(vm.isMonthSelectOpened.drop(1)) { isOpen ->
            if (isOpen) binding.monthSelect.fadeIn()
            else binding.monthSelect.fadeOut()

            binding.ivArrow.animate().rotation(if (isOpen) 0f else 180f).start()
        }
        vm.currentYear.value = vm.selectedYearMonth.value.first

        onSelectedCurrentYear = {
            vm.currentYear.value = it
        }

        onSelectedYearMonth = { year, month ->
            vm.selectedYearMonth.value = year to month
            vm.isMonthSelectOpened.value = false
        }

        collectFlowWhenStarted(vm.selectedYearMonth) {
            setSelectedYearMonth(it.first, it.second)
            binding.tvMonth.text = yearMonthText(it.first, it.second)
        }

        collectFlowWhenStarted(vm.currentYear) {
            setCurrentYear(it)
        }
    }

    private fun setCards() {
        collectFlowWhenStarted(vm.dailyHappicApi.dataOnlySuccess) {
            binding.clCards.removeAllViews()
            it?.sortedByDescending { it.day }?.run {
                binding.photoEmpty.root.isVisible = it.isEmpty()
                if (it.isNotEmpty()) {
                    val flowCards = Flow(context).apply {
                        id = ViewCompat.generateViewId()
                        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setHorizontalGap(px(4))
                        setVerticalGap(px(18))
                        setWrapMode(Flow.WRAP_ALIGNED)
                        setMaxElementsWrap(4)
                        setHorizontalStyle(Flow.CHAIN_SPREAD_INSIDE)
                        setVerticalStyle(Flow.CHAIN_PACKED)
                    }
                    mapIndexed { index, dailyHappic ->
                        ItemDailyHappicPhotoBinding.inflate(layoutInflater).apply {
                            root.injectViewId()
                            photo = dailyHappic
                            if (isToday(vm.selectedYearMonth.value, dailyHappic.day)) {
                                ivPhoto.strokeColor = ColorStateList.valueOf(getColor(R.color.orange))
                                ivPhoto.strokeWidth = requireContext().px(1).toFloat()
                                tvDay.setTextColor(getColor(R.color.orange))
                            }
                            root.setOnClickListener {
                                vm.detailDailyHappicIndex.value = index
                                emitEvent(navigationVm.onNavigateDetail)
                            }
                        }
                    }.forEach { itemBinding ->
                        val width = (requireContext().screenWidth - requireContext().px(55)) / 4
                        binding.clCards.addView(itemBinding.root, ConstraintLayout.LayoutParams(width, WRAP_CONTENT))
                        flowCards.addView(itemBinding.root)
                    }
                    binding.clCards.addView(flowCards)
                }
            }
        }
    }

    private fun isToday(yearMonth: Pair<Int, Int>, day: Int): Boolean =
        LocalDate.of(yearMonth.first, yearMonth.second, day) == LocalDate.now()
}

