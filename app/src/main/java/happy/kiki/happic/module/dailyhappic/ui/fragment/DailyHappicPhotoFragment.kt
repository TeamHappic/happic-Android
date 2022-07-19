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
import happy.kiki.happic.databinding.FragmentDailyHappicPhotoBinding
import happy.kiki.happic.databinding.ItemDailyHappicPhotoBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.collectFlowWhenStarted
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.fadeOut
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.core.util.yearMonthText
import happy.kiki.happic.module.dailyhappic.data.YearMonthModel
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicPhotoListModel
import kotlinx.coroutines.flow.MutableStateFlow

class DailyHappicPhotoFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicPhotoBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicPhotoBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCards()
        configureMonthSelect()
    }

    private val currentYear = MutableStateFlow(now.year)
    private val selectedYearMonth = MutableStateFlow(YearMonthModel(now.year, now.monthValue))

    private fun configureMonthSelect() {

        binding.monthSelect.apply {
            binding.borderMonth.setOnClickListener {
                with(this) {
                    if (this.isVisible) this.fadeOut()
                    else this.fadeIn()
                }
                currentYear.value = selectedYearMonth.value.year
            }


            onSelectedCurrentYear = { currentYear ->
                this@DailyHappicPhotoFragment.currentYear.value = currentYear
            }

            onSelectedYearMonth = { year, month ->
                selectedYearMonth.value = YearMonthModel(year, month)
                this.fadeOut()
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

    private fun setCards() {
        (0..30).map {
            ItemDailyHappicPhotoBinding.inflate(layoutInflater).apply {
                root.id = ViewCompat.generateViewId()
                photo = DailyHappicPhotoListModel("a", (it + 1).toString(), "https://github.com/kimdahee7.png")
            }
        }.forEach { itemBinding ->
            val width = (requireContext().screenWidth - requireContext().px(55)) / 4
            binding.clCards.addView(itemBinding.root, ConstraintLayout.LayoutParams(width, WRAP_CONTENT))
            binding.flowCards.addView(itemBinding.root)
        }
    }
}

