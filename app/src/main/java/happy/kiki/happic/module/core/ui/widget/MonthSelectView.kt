package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.isVisible
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ViewMonthSelectBinding
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.now
import java.time.LocalDate

class MonthSelectView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {
    private val monthSelectView by lazy {
        ViewMonthSelectBinding.inflate(LayoutInflater.from(context))
    }

    private var currentYear = now.year
    private val isCurrentYear get() = currentYear == LocalDate.now().year
    private var selectedYearMonth = now.year to now.monthValue

    var onSelectedCurrentYear: ((Int) -> Unit)? = null
    var onSelectedYearMonth: ((Int, Int) -> Unit)? = null

    init {
        addView(monthSelectView.root)
        monthSelectView.ivArrowPrevious.setOnClickListener {
            onSelectedCurrentYear?.invoke(currentYear - 1)
        }
        monthSelectView.ivArrowNext.setOnClickListener {
            if (currentYear != now.year) {
                onSelectedCurrentYear?.invoke(currentYear + 1)
            }
        }

        updateUiStates()
        setMonthTextViewClickListeners()
    }

    fun setCurrentYear(year: Int) {
        if (currentYear == year) return;
        currentYear = year
        updateUiStates()
    }

    fun setSelectedYearMonth(year: Int, month: Int) {
        if (selectedYearMonth == year to month) return
        selectedYearMonth = year to month
        updateUiStates()
    }

    private val monthTextViews: Sequence<TextView>
        get() = monthSelectView.clMonth.children.filter { it is TextView }.map { it as TextView }

    private fun setMonthTextViewClickListeners() = monthTextViews.forEachIndexed { index, view ->
        val monthIdx = index + 1
        view.setOnClickListener {
            onSelectedYearMonth?.invoke(currentYear, monthIdx)
        }
    }

    private fun updateUiStates() {
        monthSelectView.tvYear.text = currentYear.toString()
        monthSelectView.ivArrowNext.isVisible = !isCurrentYear
        monthTextViews.forEachIndexed { index, view ->
            val monthIdx = index + 1
            view.run {
                val isFutureMonth = isCurrentYear && monthIdx > LocalDate.now().month.value
                val isSelectedMonth = currentYear == selectedYearMonth.first && selectedYearMonth.second == monthIdx
                isClickable = !isFutureMonth
                setTextAppearance(
                    if (isSelectedMonth) R.style.Bold_16
                    else R.style.Medium_16
                )
                setTextColor(
                    getColor(
                        if (isSelectedMonth) R.color.dark_purple
                        else if (isFutureMonth) R.color.gray7
                        else R.color.gray2
                    )
                )
            }
        }
    }
}