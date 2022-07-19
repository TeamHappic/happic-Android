package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import happy.kiki.happic.databinding.FragmentDailyHappicPhotoBinding
import happy.kiki.happic.databinding.ItemDailyHappicPhotoBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.yearMonthText

class DailyHappicPhotoFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicPhotoBinding>()
    private val vm by viewModels<DailyHappicViewModel>({ requireParentFragment() })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicPhotoBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCards()
        configureMonthSelect()
    }

    private fun configureMonthSelect() {

        binding.monthSelect.apply {
            binding.borderMonth.setOnClickListener {
                with(this) {
                    if (this.isVisible) this.fadeOut()
                    else this.fadeIn()
                }
                vm.currentYear.value = vm.selectedYearMonth.value.first
            }


            onSelectedCurrentYear = { currentYear ->
                vm.currentYear.value = currentYear
            }

            onSelectedYearMonth = { year, month ->
                vm.selectedYearMonth.value = year to month
                this.fadeOut()
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

    private fun setCards() {
        collectFlowWhenStarted(vm.dailyHappicPhotosApi.data) {
            it?.run {
                map {
                    ItemDailyHappicPhotoBinding.inflate(layoutInflater).apply {
                        root.id = ViewCompat.generateViewId()
                        photo = it
                    }
                }.forEach { itemBinding ->
                    val width = (requireContext().screenWidth - requireContext().px(55)) / 4
                    binding.clCards.addView(itemBinding.root, ConstraintLayout.LayoutParams(width, WRAP_CONTENT))
                    binding.flowCards.addView(itemBinding.root)
                }
            }
        }
    }
}


