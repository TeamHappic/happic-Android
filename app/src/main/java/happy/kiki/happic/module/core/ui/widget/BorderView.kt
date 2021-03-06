package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import createRippleDrawable
import happy.kiki.happic.R
import happy.kiki.happic.module.core.util.OnChangeProp
import happy.kiki.happic.module.core.util.extension.setShadowColorIfAvailable

class BorderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ConstraintLayout(context, attrs) {

    var cornerRadius by OnChangeProp(0f) { updateUI() }
    var fillColor by OnChangeProp(Color.TRANSPARENT) { updateUI() }
    var strokeWidth by OnChangeProp(0f) { updateUI() }
    var strokeColor by OnChangeProp(Color.TRANSPARENT) { updateUI() }
    var shadowColor by OnChangeProp(Color.BLACK) { updateUI() }
    var rippleColor by OnChangeProp(context.getColor(R.color.ripple)) { updateUI() }

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
            createRippleDrawable(fillColor, rippleColor, cornerRadius).apply {
                this.setDrawable(0, shapeDrawable)
            }
        } else {
            shapeDrawable
        }

        outlineProvider = ViewOutlineProvider.BACKGROUND
        clipToOutline = true
        setShadowColorIfAvailable(shadowColor)
    }
}