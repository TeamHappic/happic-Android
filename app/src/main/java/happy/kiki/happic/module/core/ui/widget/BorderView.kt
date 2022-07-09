package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import happy.kiki.happic.R
import happy.kiki.happic.module.core.util.OnChangeProp
import happy.kiki.happic.module.core.util.extension.setShadowColorIfAvailable

fun createShapeDrawable(cornerSize: Float): Drawable {
    val model = ShapeAppearanceModel().toBuilder().setAllCorners(CornerFamily.ROUNDED, cornerSize).build()
    val drawable = MaterialShapeDrawable(model).apply {
        strokeWidth = 2f
        strokeColor = ColorStateList.valueOf(Color.WHITE)
        fillColor = ColorStateList.valueOf(Color.RED)
    }
    return drawable
}

class BorderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    private var cornerRadius by OnChangeProp(0f) { updateUI() }
    private var fillColor by OnChangeProp(Color.TRANSPARENT) { updateUI() }
    private var strokeWidth by OnChangeProp(0f) { updateUI() }
    private var strokeColor by OnChangeProp(Color.TRANSPARENT) { updateUI() }
    private var shadowColor by OnChangeProp(Color.BLACK) { updateUI() }

    init {
        attrs?.let { a ->
            context.theme.obtainStyledAttributes(a, R.styleable.BorderView, 0, 0).apply {
                try {
                    cornerRadius = getDimension(R.styleable.BorderView_card_corner_radius, cornerRadius)
                    fillColor = getColor(R.styleable.BorderView_card_background_color, fillColor)
                    strokeWidth = getDimension(R.styleable.BorderView_card_stroke_width, strokeWidth)
                    strokeColor = getColor(R.styleable.BorderView_card_stroke_color, strokeColor)
                    shadowColor = getColor(R.styleable.BorderView_card_shadow_color, shadowColor)
                } finally {
                    recycle()
                }
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }

    private fun updateUI() {
        background = MaterialShapeDrawable(ShapeAppearanceModel().withCornerSize(cornerRadius)).apply {
            fillColor = ColorStateList.valueOf(this@BorderView.fillColor)
            strokeWidth = this@BorderView.strokeWidth
            strokeColor = ColorStateList.valueOf(this@BorderView.strokeColor)
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