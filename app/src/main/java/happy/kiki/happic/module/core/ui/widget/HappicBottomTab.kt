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
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import happy.kiki.happic.R
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.px
import happy.kiki.happic.module.core.util.extension.pxFloat
import happy.kiki.happic.module.core.util.extension.setShadowColorIfAvailable
import kotlin.properties.Delegates

class HappicBottomTab @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    init {
        orientation = HORIZONTAL
        setBackgroundColor(context.getColor(R.color.bg_black2))
        addViews()
        applySelectedState()
    }

    var onTabSelectedListener: ((Int) -> Unit)? = null
    var selectedTabIndex by Delegates.observable(0) { _, prev, cur ->
        if (prev != cur) {
            onTabSelectedListener?.invoke(cur)
            applySelectedState()
        }
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

        ConstraintSet().also { set ->
            set.clone(this@apply)
            set.centerHorizontally(imageView.id, ConstraintSet.PARENT_ID)
            set.centerHorizontally(textView.id, ConstraintSet.PARENT_ID)
            set.connect(imageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, px(12))
            set.connect(textView.id, ConstraintSet.TOP, imageView.id, ConstraintSet.BOTTOM, px(4))
        }.applyTo(this)
    }

    private fun addViews() {
        addView(createMenuButton(R.drawable.hp_ic_home, "홈", 0))
        addView(createMenuButton(R.drawable.hp_ic_dh, "하루해픽", 1))
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
        addView(createMenuButton(R.drawable.hp_ic_hr, "해픽레포트", 2))
        addView(createMenuButton(R.drawable.hp_ic_set, "설정", 3))
    }

    private fun applySelectedState() {

    }
}