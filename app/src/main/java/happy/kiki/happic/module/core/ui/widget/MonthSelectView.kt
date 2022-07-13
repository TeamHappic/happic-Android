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

    init {
        addView(monthSelectView.root)
        monthSelectView.ivArrowPrevious.setOnClickListener { onClickLeftArrow?.invoke() }
        monthSelectView.ivArrowNext.setOnClickListener { onClickRightArrow?.invoke() }

        updateUiStates()
        setMonthTextViewClickListeners()
    }

    private var currentYear = now.year
    private val isCurrentYear get() = currentYear == LocalDate.now().year

    private var selectedYearMonth = now.year to now.monthValue

    var onClickLeftArrow: (() -> Unit)? = null
    var onClickRightArrow: (() -> Unit)? = null
    var onClickMonthText: ((Int) -> Unit)? = null

    fun setCurrentYear(year: Int) {
        currentYear = year
        monthSelectView.tvYear.text = year.toString()
        updateUiStates()
    }

    fun setSelectedYearMonth(year: Int, month: Int) {
        selectedYearMonth = year to month
        updateUiStates()
    }

    private val monthTextViews: Sequence<TextView>
        get() = monthSelectView.clMonth.children.filter { it is TextView }.map { it as TextView }

    private fun setMonthTextViewClickListeners() = monthTextViews.forEachIndexed { index, view ->
        val monthIdx = index + 1
        view.setOnClickListener {
            onClickMonthText?.invoke(monthIdx)
        }
    }

    private fun updateUiStates() {
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