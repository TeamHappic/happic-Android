package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.children
import happy.kiki.happic.R
import happy.kiki.happic.databinding.ViewMonthSelectBinding
import happy.kiki.happic.module.core.util.extension.getColor
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

        updateTextUiStates()
        setMonthTextViewClickListeners()
    }

    private var year = LocalDate.now().year
    private var selectedMonth = LocalDate.now().monthValue
    private val isCurrentYear get() = year == LocalDate.now().year

    var onClickLeftArrow: (() -> Unit)? = null
    var onClickRightArrow: (() -> Unit)? = null
    var onClickMonthText: ((Int) -> Unit)? = null

    fun setYear(year: Int) {
        this.year = year
        this.monthSelectView.tvYear.text = year.toString()
        updateTextUiStates()
    }

    fun setMonthSelected(month: Int) {
        selectedMonth = month
        updateTextUiStates()
    }

    private val monthTextViews: Sequence<TextView>
        get() = monthSelectView.clMonth.children.filter { it is TextView }.map { it as TextView }

    private fun setMonthTextViewClickListeners() = monthTextViews.forEachIndexed { index, view ->
        val monthIdx = index + 1
        view.setOnClickListener {
            onClickMonthText?.invoke(monthIdx)
        }
    }

    private fun updateTextUiStates() = monthTextViews.forEachIndexed { index, view ->
        val monthIdx = index + 1
        view.run {
            val isFutureMonth = isCurrentYear && monthIdx > LocalDate.now().month.value
            isClickable = !isFutureMonth
            setTextAppearance(
                if (selectedMonth == monthIdx) R.style.Bold_16
                else R.style.Medium_16
            )
            setTextColor(
                getColor(
                    if (selectedMonth == monthIdx) R.color.dark_purple
                    else if (isFutureMonth) R.color.gray7
                    else R.color.white
                )
            )
        }
    }

}