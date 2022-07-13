package happy.kiki.happic.module.report.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentReportBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.yearMonthText
import kotlinx.coroutines.flow.drop

class ReportFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentReportBinding>()
    private val viewModel by viewModels<ReportViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentReportBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        configureHeader()
        configureMonthSelect()
        configureUI()
    }

    private fun configureHeader() = binding.header.apply {
        setOnClickListener {
            viewModel.isMonthSelectOpened.value = !viewModel.isMonthSelectOpened.value
        }
        collectFlowWhenStarted(viewModel.isMonthSelectOpened.drop(1)) { isOpen ->
            if (isOpen) binding.monthSelect.fadeIn()
            else binding.monthSelect.fadeOut()
        }
    }

    private fun configureMonthSelect() = binding.monthSelect.let { monthSelect ->
        collectFlowWhenStarted(viewModel.currentYear) {
            monthSelect.setCurrentYear(it)
        }
        collectFlowWhenStarted(viewModel.selectedYearMonth) { (year, month) ->
            binding.yearMonth.text = yearMonthText(year, month)
            monthSelect.setSelectedYearMonth(year, month)
        }
        monthSelect.onSelectedCurrentYear = {
            viewModel.currentYear.value = it
        }
        monthSelect.onSelectedYearMonth = { year, month ->
            viewModel.selectedYearMonth.value = year to month
        }
    }

    private fun configureUI() {
        binding.sectionMomentText.text = buildSpannedString {
            append("이번 달 베스트")
            color(getColor(R.color.orange)) {
                append(" 해픽 ")
            }
            append("모멘트는 이거야!")
        }
    }
}