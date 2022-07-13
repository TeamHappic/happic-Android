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
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyHappicPhotoFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicPhotoBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicPhotoBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        setCards()
        configureMonthSelect()
    }

    private fun initData() {
        with(binding) {
            val now = LocalDate.now()
            tvMonth.text = now.format(DateTimeFormatter.ofPattern("yyyy.MM"))
                .toString() //            monthSelectContainer.tvYear.text = now.year.toString()
        }
    }

    private val year = MutableStateFlow(LocalDate.now().year)
    private val selectedMonth = MutableStateFlow(LocalDate.now().monthValue)

    private fun configureMonthSelect() {

        binding.monthSelect.apply {
            binding.borderMonth.setOnClickListener {
                with(this) {
                    if (this.isVisible) this.fadeOut()
                    else this.fadeIn()
                }
            }
            
            onClickRightArrow = {
                val currentYear = LocalDate.now().year
                if (year.value < currentYear) year.value += 1
            }
            onClickLeftArrow = {
                year.value -= 1
            }

            collectFlowWhenStarted(selectedMonth) {
                setMonthSelected(it)
            }
            collectFlowWhenStarted(year) {
                setYear(it)
            }
            onClickMonthText = {
                binding.monthSelect.setMonthSelected(it)
            }
        }
    }

    private fun setCards() {
        (0..30).map {
            ItemDailyHappicPhotoBinding.inflate(layoutInflater).apply {
                root.id = ViewCompat.generateViewId()
            }
        }.forEach { itemBinding ->
            val width = (requireContext().screenWidth - requireContext().px(55)) / 4
            binding.clCards.addView(itemBinding.root, ConstraintLayout.LayoutParams(width, WRAP_CONTENT))
            binding.flowCards.addView(itemBinding.root)
        }
    }
}

