package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButton.ICON_GRAVITY_TEXT_END
import com.google.android.material.progressindicator.CircularProgressIndicator
import happy.kiki.happic.R
import happy.kiki.happic.module.core.util.OnChangeProp
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.pxFloat
import happy.kiki.happic.module.core.util.extension.setShadowColorIfAvailable
import happy.kiki.happic.module.core.util.getEnum

class RoundButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    enum class Type(@ColorRes val color: Int) {
        DARK_BLUE(R.color.dark_blue), DARK_PURPLE(R.color.dark_purple), ORANGE(R.color.orange), KAKAO_YELLOW(R.color.kakao_yellow), ORANGE_REVERSE(
            R.color.bg_black1
        );

        val textColor: Int
            get() = when (this) {
                ORANGE -> R.color.gray0
                KAKAO_YELLOW -> R.color.gray9
                ORANGE_REVERSE -> R.color.orange
                else -> R.color.white
            }
    }

    var isLoading by OnChangeProp(false) { updateUI() }
    var type: Type by OnChangeProp(Type.DARK_BLUE) { updateUI() }
    var text: String by OnChangeProp("") { updateUI() }
    var icon: Drawable? by OnChangeProp(null) { updateUI() }

    private val button by lazy {
        MaterialButton(context).apply {
            insetTop = 0
            insetBottom = 0
            cornerRadius = px(8)
            setTextAppearance(R.style.Bold_16)
            setTextColor(getColor(R.color.white))
            setShadowColorIfAvailable(getColor(R.color.white))
            setRippleColorResource(R.color.white)
            iconGravity = ICON_GRAVITY_TEXT_END
            iconPadding = px(4)
            iconSize = px(20)
            iconTint = null
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
    }

    private val indicator by lazy {
        CircularProgressIndicator(context).apply {
            indicatorSize = px(24)
            setIndicatorColor(getColor(R.color.white))
            isIndeterminate = true
            layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER)
            elevation = pxFloat(24)
            outlineProvider = null
        }
    }

    init {
        addView(button)
        attrs?.let { a ->
            context.theme.obtainStyledAttributes(a, R.styleable.RoundButton, 0, 0).apply {
                try {
                    type = getEnum(R.styleable.RoundButton_type, Type.values().first())
                    text = getString(R.styleable.RoundButton_text) ?: ""
                    icon = getDrawable(R.styleable.RoundButton_icon)
                } finally {
                    recycle()
                }
            }
        }
        updateUI()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(px(48), MeasureSpec.EXACTLY))
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }

    private fun updateUI() {
        button.apply {
            backgroundTintList = ColorStateList.valueOf(getColor(type.color))
            setTextColor(if (isLoading) Color.TRANSPARENT else getColor(type.textColor))
        }

        if (isLoading) {
            button.apply {
                icon = null
                text = ""
                isEnabled = false
            }
            addView(indicator)
        } else {
            button.run {
                text = this@RoundButton.text
                icon = this@RoundButton.icon
                isEnabled = true
            }
            removeView(indicator)
        }
    }
}