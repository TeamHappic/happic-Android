import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import happy.kiki.happic.R
import happy.kiki.happic.databinding.FragmentDailyHappicPhotoBinding
import happy.kiki.happic.databinding.ItemDailyHappicPhotoBinding
import happy.kiki.happic.module.core.util.AutoCleardValue
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.screenWidth
import happy.kiki.happic.module.core.util.fadeIn
import happy.kiki.happic.module.core.util.fadeOut
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DailyHappicPhotoFragment : Fragment() {
    private var binding by AutoCleardValue<FragmentDailyHappicPhotoBinding>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentDailyHappicPhotoBinding.inflate(inflater, container, false).let { binding = it; it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initData()
        setCurrentMonths()
        setCards()
        setOnClickListener()
    }

    private fun initData() {
        with(binding) {
            val now = LocalDate.now()
            tvMonth.text = now.format(DateTimeFormatter.ofPattern("yyyy.MM")).toString()
            monthSelectContainer.tvYear.text = now.year.toString()
        }
    }

    private fun setOnClickListener() {
        binding.borderMonth.setOnClickListener {
            with(binding.monthSelectContainer.monthSelection) {
                if (this.isVisible) this.fadeOut()
                else this.fadeIn()
            }
        }

        with(binding.monthSelectContainer) {
            ivArrowPrevious.setOnClickListener {
                val year = tvYear.text.toString().toInt() - 1
                setMonthTransactionEvent(year)
            }
            ivArrowNext.setOnClickListener {
                val year = tvYear.text.toString().toInt() + 1
                setMonthTransactionEvent(year)
            }
        }

    }

    private fun setMonthTransactionEvent(year: Int) {
        with(binding.monthSelectContainer) {
            tvYear.text = year.toString()
            val isNotCurrentYear = year < LocalDate.now().year
            ivArrowNext.isVisible = isNotCurrentYear
            clMonth.removeAllViews() //            setCurrentMonths()
            setPastMonths() //            flowMonth.allViews.forEach {
            //                flowMonth.removeView(it)
            //            }
            //            if (isNotCurrentYear) setPastMonths() else setCurrentMonths()
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

    private fun setCurrentMonths() {
        binding.monthSelectContainer.ivArrowNext.isVisible = false
        val month = LocalDate.now().month.value
        var textStyle = R.style.Medium_16
        var isTextClickable = true
        var textColor = R.color.gray2
        (0..11).map {
            if (it + 1 == month) {
                textStyle = R.style.Bold_16
                textColor = R.color.dark_purple
            } else if (it + 1 > month) {
                textStyle = R.style.Medium_16
                isTextClickable = false
                textColor = R.color.gray7
            }

            val textView = TextView(ContextThemeWrapper(context, textStyle)).apply {
                isClickable = isTextClickable
                id = ViewCompat.generateViewId()
                includeFontPadding = true
                "${it + 1}월".also { text = it }
                setTextColor(context.getColor(textColor))
            }
            with(binding.monthSelectContainer) {
                clMonth.addView(textView, ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
                flowMonth.addView(textView)
            }
        }
    }

    private fun setPastMonths() {
        (0..11).map {
            val textView = TextView(ContextThemeWrapper(context, R.style.Medium_16)).apply {
                isClickable = true
                id = ViewCompat.generateViewId()
                includeFontPadding = true
                "${it + 1}월".also { text = it }
                setTextColor(context.getColor(R.color.gray2))
            }
            with(binding.monthSelectContainer) {
                clMonth.addView(textView, ConstraintLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
                flowMonth.addView(textView)
            }
        }
    }

}

