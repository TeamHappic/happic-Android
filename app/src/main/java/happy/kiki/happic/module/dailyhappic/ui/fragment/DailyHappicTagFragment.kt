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
import androidx.fragment.app.viewModels
import happy.kiki.happic.databinding.FragmentDailyHappicTagBinding
import happy.kiki.happic.databinding.ItemDailyHappicTagBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.yearMonthText
import happy.kiki.happic.module.report.util.koFormat
import java.time.LocalDate

class DailyHappicTagFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicTagBinding>()
    private val vm by viewModels<DailyHappicViewModel>({ requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicTagBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureMonthSelect()
        setTags()
    }

    private fun configureMonthSelect() {

        binding.monthSelect.apply {
            binding.borderMonth.setOnClickListener {
                with(this) {
                    if (isVisible) fadeOut()
                    else fadeIn()
                }
                vm.currentYear.value = vm.selectedYearMonth.value.first
            }

            onSelectedCurrentYear = { currentYear ->
                vm.currentYear.value = currentYear
            }

            onSelectedYearMonth = { year, month ->
                vm.selectedYearMonth.value = year to month
                fadeOut()
            }

            collectFlowWhenStarted(vm.selectedYearMonth) {
                setSelectedYearMonth(it.first, it.second)
                binding.tvMonth.text = yearMonthText(it.first, it.second)
            }

            collectFlowWhenStarted(vm.currentYear) {
                setCurrentYear(it)
            }
        }
    }

    private fun setTags() {
        collectFlowWhenStarted(vm.dailyHappicTagsApi.data) {
            it?.run {
                binding.llTags.removeAllViews()
                map {
                    ItemDailyHappicTagBinding.inflate(layoutInflater).apply {
                        root.id = ViewCompat.generateViewId()
                        tag = it
                        day = getDay(it.date).toString()
                        dayOfWeek = getDayOfWeek(it.date)
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

    private fun getDay(date: String): Int? {
        date.split("-", " ").apply {
            if (size > 3) {
                return this[2].toInt()
            }
            return null
        }
    }

    private fun getDayOfWeek(date: String): String? {
        date.split("-", " ").apply {
            if (size > 3) {
                return LocalDate.of(this[0].toInt(), this[1].toInt(), this[2].toInt()).dayOfWeek.koFormat
            }
            return null
        }
    }
}