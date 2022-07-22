package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.constraintlayout.widget.ConstraintLayout
import com.aigestudio.wheelpicker.WheelPicker
import happy.kiki.happic.R
import happy.kiki.happic.module.core.ui.widget.util.applyConstraint
import happy.kiki.happic.module.core.ui.widget.util.bottomToParent
import happy.kiki.happic.module.core.ui.widget.util.endToParent
import happy.kiki.happic.module.core.ui.widget.util.endToStart
import happy.kiki.happic.module.core.ui.widget.util.startToEnd
import happy.kiki.happic.module.core.ui.widget.util.startToParent
import happy.kiki.happic.module.core.ui.widget.util.topToParent
import happy.kiki.happic.module.core.util.OnChangeProp
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.injectViewId
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.now

class TimeWheelPicker @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {

    var onHourChangedListener: ((Int) -> Unit)? = null
    var hour by OnChangeProp(now.hour) {
        bindHourToUi()
        onHourChangedListener?.invoke(it)
    }

    private val leftWheel by lazy {
        WheelPicker(context).apply {
            data = listOf("오전", "오후")
            injectViewId()
            isCurved = false
            isCyclic = false
            selectedItemTextColor = Color.parseColor("#F2FFFFFF")
            itemTextColor = getColor(R.color.gray8)

            setOnItemSelectedListener { picker, data, position ->
                hour = hour % 12 + position * 12
            }
        }
    }

    private val divider by lazy {
        View(context).apply {
            layoutParams = LayoutParams(px(1), MATCH_PARENT)
            setPadding(0, px(4), 0, px(4))
            injectViewId()
        }
    }

    private val rightWheel by lazy {
        WheelPicker(context).apply {
            data = (0..23).map {
                (it % 12).let {
                    if (it == 0) 12
                    else it
                }.toString()
            }
            injectViewId()
            visibleItemCount = 5
            isCurved = true
            isCyclic = true
            selectedItemTextColor = Color.parseColor("#F2FFFFFF")
            itemTextColor = getColor(R.color.gray8)

            setOnItemSelectedListener { picker, data, position ->
                hour = position
            }
        }
    }

    init {
        setBackgroundColor(getColor(R.color.bg_black2))
        addViews()
        bindHourToUi()
    }

    private fun addViews() {
        addView(leftWheel)
        addView(divider)
        addView(rightWheel)

        applyConstraint {
            startToParent(divider)
            endToParent(divider)

            startToParent(leftWheel)
            endToStart(leftWheel, divider)
            topToParent(leftWheel)
            bottomToParent(leftWheel)

            startToEnd(rightWheel, divider)
            endToParent(rightWheel)
            topToParent(rightWheel)
            bottomToParent(rightWheel)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(px(220), EXACTLY))
    }

    private fun bindHourToUi() {
        leftWheel.selectedItemPosition = if (hour < 12) 0 else 1
        rightWheel.selectedItemPosition = hour
    }
}