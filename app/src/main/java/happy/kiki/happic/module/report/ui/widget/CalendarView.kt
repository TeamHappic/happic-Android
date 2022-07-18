package happy.kiki.happic.module.report.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.google.android.material.textview.MaterialTextView
import happy.kiki.happic.R
import happy.kiki.happic.module.core.ui.widget.util.applyConstraint
import happy.kiki.happic.module.core.ui.widget.util.centerParent
import happy.kiki.happic.module.core.util.OnChangeProp
import happy.kiki.happic.module.core.util.debugE
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.now
import happy.kiki.happic.module.report.util.calculateRequiredRow
import happy.kiki.happic.module.report.util.dayOfWeekIndex
import java.time.LocalDate

class CalendarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    var yearMonth by OnChangeProp(now.year to now.monthValue) {
        this.invalidateChildren()
    }
    var coloredDate by OnChangeProp(hashSetOf<Int>()) {
        this.invalidateChildren()
    }

    private val texts = mutableListOf<MaterialTextView>()

    init {
        initView()
        this.invalidateChildren()
        this.invalidateChildren()
    }

    private fun generateRowLayout(
        data: List<String> = List(7) { "" },
        @ColorRes textColor: Int = R.color.gray5,
        addToTextViewList: Boolean = false
    ): ViewGroup {
        assert(data.size == 7)

        val container = LinearLayout(context).apply {
            orientation = HORIZONTAL
            layoutParams = LayoutParams(MATCH_PARENT, px(36))
        }

        data.map { s ->
            ConstraintLayout(context).apply {
                val tv = MaterialTextView(context).apply {
                    text = s
                    setTextAppearance(R.style.C2_P_M12)
                    setTextColor(getColor(textColor))
                    textAlignment = TEXT_ALIGNMENT_CENTER
                    id = ViewCompat.generateViewId()
                }
                if (addToTextViewList) texts.add(tv)

                val iv = ImageView(context).apply {
                    setImageResource(R.drawable.hp_img_calenderpoint)
                    scaleType = CENTER_INSIDE
                    isVisible = false
                    id = ViewCompat.generateViewId()
                }
                addView(iv)
                addView(tv)

                applyConstraint {
                    centerParent(tv)
                    centerParent(iv)
                }
            }
        }.forEach { container.addView(it, LayoutParams(0, MATCH_PARENT, 1f)) }

        return container
    }

    private fun initView() {
        orientation = VERTICAL
    }

    private fun invalidateChildren() {
        texts.clear()
        removeAllViews()
        addView(generateRowLayout(listOf("일", "월", "화", "수", "목", "금", "토")))
        repeat(calculateRequiredRow(LocalDate.of(yearMonth.first, yearMonth.second, 1))) {
            addView(generateRowLayout(textColor = R.color.gray4, addToTextViewList = true))
        }

        val currentMonthFirstDay = LocalDate.of(yearMonth.first, yearMonth.second, 1)
        val startIndex = currentMonthFirstDay.dayOfWeekIndex
        debugE(startIndex)
        val dayCountInMonth = currentMonthFirstDay.lengthOfMonth()

        for (i in startIndex until startIndex + dayCountInMonth) {
            texts[i].text = (i - startIndex + 1).toString()
        }
        texts.forEach { it.setTextColor(getColor(R.color.gray4)) }

        texts.forEach { tv ->
            tv.text.toString().toIntOrNull()?.let { n ->
                if (n in coloredDate) {
                    (tv.parent as ViewGroup).getChildAt(0).isVisible = true
                }
            }
        }
    }
}