package happy.kiki.happic.module.core.ui.widget

import android.R.attr
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import happy.kiki.happic.R
import happy.kiki.happic.module.core.util.OnChangeProp
import happy.kiki.happic.module.core.util.extension.setShadowColorIfAvailable

class BorderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    private var cornerRadius by OnChangeProp(0f) { updateUI() }
    private var fillColor by OnChangeProp(Color.TRANSPARENT) { updateUI() }
    private var strokeWidth by OnChangeProp(0f) { updateUI() }
    private var strokeColor by OnChangeProp(Color.TRANSPARENT) { updateUI() }
    private var shadowColor by OnChangeProp(Color.BLACK) { updateUI() }
    private var rippleColor by OnChangeProp(Color.LTGRAY) { updateUI() }

    init {
        attrs?.let { a ->
            context.theme.obtainStyledAttributes(a, R.styleable.BorderView, 0, 0).apply {
                try {
                    cornerRadius = getDimension(R.styleable.BorderView_card_corner_radius, cornerRadius)
                    fillColor = getColor(R.styleable.BorderView_card_background_color, fillColor)
                    strokeWidth = getDimension(R.styleable.BorderView_card_stroke_width, strokeWidth)
                    strokeColor = getColor(R.styleable.BorderView_card_stroke_color, strokeColor)
                    shadowColor = getColor(R.styleable.BorderView_card_shadow_color, shadowColor)
                    rippleColor = getColor(R.styleable.BorderView_card_ripple_color, rippleColor)
                } finally {
                    recycle()
                }
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    private fun getPressedColorSelector(normalColor: Int, pressedColor: Int): ColorStateList {
        return ColorStateList(
            arrayOf(
                intArrayOf(attr.state_pressed),
                intArrayOf(attr.state_focused),
                intArrayOf(attr.state_activated),
                intArrayOf()
            ), intArrayOf(
                pressedColor, pressedColor, pressedColor, normalColor
            )
        )
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
        updateUI()
    }

    private fun updateUI() {
        val shapeDrawable = MaterialShapeDrawable(ShapeAppearanceModel().withCornerSize(cornerRadius)).apply {
            fillColor = ColorStateList.valueOf(this@BorderView.fillColor)
            strokeWidth = this@BorderView.strokeWidth
            strokeColor = ColorStateList.valueOf(this@BorderView.strokeColor)
        }

        background = if (hasOnClickListeners()) {
            RippleDrawable(
                getPressedColorSelector(fillColor, rippleColor), ColorDrawable(fillColor), null
            ).apply {
                this.setDrawable(0, shapeDrawable)
            }
        } else {
            shapeDrawable
        }

        outlineProvider = ViewOutlineProvider.BACKGROUND
        clipToOutline = true
        setShadowColorIfAvailable(shadowColor)
        setPadding(
            kotlin.math.max(strokeWidth, paddingLeft.toFloat()).toInt(),
            kotlin.math.max(strokeWidth, paddingTop.toFloat()).toInt(),
            kotlin.math.max(strokeWidth, paddingRight.toFloat()).toInt(),
            kotlin.math.max(strokeWidth, paddingBottom.toFloat()).toInt()
        )
    }
}