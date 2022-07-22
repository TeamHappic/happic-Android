package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentDailyHappicTagBinding
import happy.kiki.happic.databinding.ItemDailyHappicTagBinding
import happy.kiki.happic.module.core.util.AutoClearedValue
import happy.kiki.happic.module.core.util.emitEvent
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.core.util.yearMonthText
import happy.kiki.happic.module.report.util.koFormat
import kotlinx.coroutines.flow.drop
import java.time.LocalDate

class DailyHappicTagFragment : Fragment() {
    private var binding by AutoClearedValue<FragmentDailyHappicTagBinding>()
    private val vm by activityViewModels<DailyHappicViewModel>()
    private val navigationVm by activityViewModels<DailyHappicNavigationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicTagBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureMonthSelect()
        setTags()
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

    private fun setTags() {
        collectFlowWhenStarted(vm.dailyHappicApi.dataOnlySuccess) {
            val (year, month) = vm.selectedYearMonth.value

            binding.llTags.removeAllViews()
            it?.run {
                binding.photoEmpty.root.isVisible = it.isEmpty()
                mapIndexed { index, dailyHappic ->
                    ItemDailyHappicTagBinding.inflate(layoutInflater).apply {
                        root.id = ViewCompat.generateViewId()
                        tvWhen.text = dailyHappic.hour.koFormat
                        tag = dailyHappic

                        val date = LocalDate.of(year, month, dailyHappic.day)
                        day = dailyHappic.day.toString()
                        dayOfWeek = date.dayOfWeek.koFormat
                        if (date == now.toLocalDate()) {
                            listOf(
                                tvDate, tvDayOfWeek
                            ).forEach { textView -> textView.setTextColor(getColor(R.color.orange)) }
                        }

                        root.setOnClickListener {
                            vm.detailDailyHappicIndex.value = index
                            emitEvent(navigationVm.onNavigateDetail)
                        }
                    }
                }.forEach { itemBinding ->
                    binding.llTags.addView(
                        itemBinding.root,
                        ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                    )
                }
            }
        }
    }
}