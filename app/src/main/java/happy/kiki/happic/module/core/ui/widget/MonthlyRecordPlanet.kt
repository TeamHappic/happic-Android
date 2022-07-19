package happy.kiki.happic.module.core.ui.widget

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import com.google.android.material.textview.MaterialTextView
import happy.kiki.happic.R
import happy.kiki.happic.module.core.ui.widget.util.applyConstraint
import happy.kiki.happic.module.core.ui.widget.util.bottomToParent
import happy.kiki.happic.module.core.ui.widget.util.centerHorizontallyParent
import happy.kiki.happic.module.core.ui.widget.util.topToBottom
import happy.kiki.happic.module.core.ui.widget.util.topToParent
import happy.kiki.happic.module.core.util.OnChangeProp
import happy.kiki.happic.module.core.util.extension.fadeIn
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.now
import kotlin.math.roundToInt

class MonthlyRecordPlanet @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {
    private val monthTextView = MaterialTextView(context).apply {
        id = ViewCompat.generateViewId()
        setTextAppearance(R.style.B2_P_M14)
        setTextColor(getColor(R.color.gray2))
    }

    private val countTextView = MaterialTextView(context).apply {
        setTextAppearance(R.style.H1_G_B20)
        id = ViewCompat.generateViewId()
        setTextColor(getColor(R.color.gray2))
    }

    var month by OnChangeProp(now.monthValue) { updateUI() }
    var count by OnChangeProp(0) { updateUI() }

    private var animator: ValueAnimator? = null
    private var size by OnChangeProp(px(100)) {
        if (layoutParams == null) return@OnChangeProp
        animator?.cancel()
        animator = ValueAnimator.ofFloat(width.toFloat(), it.toFloat()).apply {
            addUpdateListener {
                val animatedSize = (it.animatedValue as Float).roundToInt()
                updateLayoutParams {
                    width = animatedSize
                    height = animatedSize
                }
            }
            interpolator = BounceInterpolator()
            duration = 1500L
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

    init {
        addViews()
        updateUI()
        fadeIn()
    }

    private fun updateUI() {
        drawPlanet()
        monthTextView.text = "${month}월"
        countTextView.text = "${count}회"
        size = calculateSize()
    }

    private fun calculateSize() = px((100 + 3.333 * count).roundToInt())

    private fun drawPlanet() {
        setBackgroundResource(if (count > 0) R.drawable.bg_monthly_planet else R.drawable.bg_monthly_empty)
    }

    private fun addViews() {
        val container = ConstraintLayout(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER)
        }
        addView(container)
        container.addView(monthTextView)
        container.addView(countTextView)

        container.applyConstraint {
            centerHorizontallyParent(monthTextView)
            centerHorizontallyParent(countTextView)
            topToParent(monthTextView)
            topToBottom(countTextView, monthTextView)
            bottomToParent(countTextView)
        }
    }
}