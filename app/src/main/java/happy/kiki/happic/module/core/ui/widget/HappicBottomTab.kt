package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import happy.kiki.happic.R
import happy.kiki.happic.module.core.ui.widget.util.applyConstraint
import happy.kiki.happic.module.core.ui.widget.util.centerHorizontallyParent
import happy.kiki.happic.module.core.ui.widget.util.topToBottom
import happy.kiki.happic.module.core.ui.widget.util.topToParent
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.pxFloat
import happy.kiki.happic.module.core.util.extension.setShadowColorIfAvailable
import kotlin.properties.Delegates

class HappicBottomTab @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    private val buttons = mutableListOf<BorderView>()

    var onTabSelectedListener: ((Int) -> Unit)? = null
    var selectedTabIndex by Delegates.observable(0) { _, prev, cur ->
        if (prev != cur) {
            onTabSelectedListener?.invoke(cur)
            applySelectedState()
        }
    }


    init {
        orientation = HORIZONTAL
        setBackgroundColor(context.getColor(R.color.bg_black2))
        addViews()
        applySelectedState()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        onTabSelectedListener = null
    }

    private fun createMenuButton(@DrawableRes menuIcon: Int, name: String, index: Int) = BorderView(context).apply {
        cornerRadius = pxFloat(6)
        layoutParams = LayoutParams(0, MATCH_PARENT, 1f)
        rippleColor = Color.TRANSPARENT
        isClickable = true
        isFocusable = true
        setOnClickListener {
            selectedTabIndex = index
        }

        val imageView = ImageView(context).apply {
            id = ViewCompat.generateViewId()
            setImageResource(menuIcon)
        }
        val textView = MaterialTextView(context).apply {
            setTextAppearance(R.style.C2_P_M12)
            id = ViewCompat.generateViewId()
            text = name
            setTextColor(context.getColor(R.color.gray5))
        }
        addView(imageView, px(20), px(20))
        addView(textView, WRAP_CONTENT, WRAP_CONTENT)

        applyConstraint {
            centerHorizontallyParent(imageView)
            centerHorizontallyParent(textView)
            topToParent(imageView, px(12))
            topToBottom(textView, imageView, px(4))
        }
    }

    private fun addViews() {
        addView(createMenuButton(R.drawable.hp_ic_home_off, "홈", 0).also { buttons.add(it) })
        addView(createMenuButton(R.drawable.hp_ic_dh_off, "하루해픽", 1).also { buttons.add(it) })
        addView(FrameLayout(context).apply {
            val fab = FloatingActionButton(context).apply {
                size = FloatingActionButton.SIZE_MINI
                backgroundTintList = ColorStateList.valueOf(getColor(R.color.dark_purple))
                setImageResource(R.drawable.plus_gray9_16)
                setMaxImageSize(px(16))
                setShadowColorIfAvailable(getColor(R.color.dark_purple))
            }
            addView(fab, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            })
        }, LayoutParams(0, MATCH_PARENT, 1f))
        addView(createMenuButton(R.drawable.hp_ic_hr_off, "해픽레포트", 2).also { buttons.add(it) })
        addView(createMenuButton(R.drawable.hp_ic_set_off, "설정", 3).also { buttons.add(it) })
    }

    private fun applySelectedState() {
        (buttons[0].getChildAt(0) as? ImageView)?.setImageResource(if (selectedTabIndex == 0) R.drawable.hp_ic_home_on else R.drawable.hp_ic_home_off)
        (buttons[1].getChildAt(0) as? ImageView)?.setImageResource(if (selectedTabIndex == 1) R.drawable.hp_ic_dh_on else R.drawable.hp_ic_dh_off)
        (buttons[2].getChildAt(0) as? ImageView)?.setImageResource(if (selectedTabIndex == 2) R.drawable.hp_ic_hr_on else R.drawable.hp_ic_hr_off)
        (buttons[3].getChildAt(0) as? ImageView)?.setImageResource(if (selectedTabIndex == 3) R.drawable.hp_ic_set_on else R.drawable.hp_ic_set_off)
    }
}