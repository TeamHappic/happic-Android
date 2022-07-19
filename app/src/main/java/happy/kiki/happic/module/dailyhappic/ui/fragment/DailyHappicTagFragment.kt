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
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.core.util.yearMonthText
import happy.kiki.happic.module.dailyhappic.data.YearMonthModel
import kotlinx.coroutines.flow.MutableStateFlow

class DailyHappicTagFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicTagBinding>()
    private val vm by viewModels<DailyHappicViewModel>({ requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicTagBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        configureMonthSelect()
        setTags()
    }

    private val currentYear = MutableStateFlow(now.year)
    private val selectedYearMonth = MutableStateFlow(YearMonthModel(now.year, now.monthValue))

    private fun configureMonthSelect() {

        binding.monthSelect.apply {
            binding.borderMonth.setOnClickListener {
                with(this) {
                    if (isVisible) fadeOut()
                    else fadeIn()
                }
                currentYear.value = selectedYearMonth.value.year
            }

            onSelectedCurrentYear = { currentYear ->
                this@DailyHappicTagFragment.currentYear.value = currentYear
            }

            onSelectedYearMonth = { year, month ->
                selectedYearMonth.value = YearMonthModel(year, month)
                fadeOut()
            }

            collectFlowWhenStarted(selectedYearMonth) {
                setSelectedYearMonth(it.year, it.month)
                binding.tvMonth.text = yearMonthText(it.year, it.month)
            }

            collectFlowWhenStarted(currentYear) {
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