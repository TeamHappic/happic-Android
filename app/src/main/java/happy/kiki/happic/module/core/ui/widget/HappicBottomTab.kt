package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import createRippleDrawable
import happy.kiki.happic.R
import happy.kiki.happic.module.core.util.extension.px

class HappicBottomTab @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    init {
        orientation = HORIZONTAL
        setBackgroundColor(context.getColor(R.color.bg_black2))
        addViews()
    }

    private fun createMenuButton(@DrawableRes menuIcon: Int, name: String) = ConstraintLayout(context).apply {
        layoutParams = LayoutParams(0, MATCH_PARENT, 1f)

        background = createRippleDrawable(context.getColor(R.color.bg_black2), context.getColor(R.color.white))
        isClickable = true
        isFocusable = true

        val imageView = ImageView(context).apply {
            id = ViewCompat.generateViewId()
            setImageResource(menuIcon)
        }
        val textView = TextView(context, null, R.style.Medium_12).apply {
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
        addView(createMenuButton(R.drawable.hp_ic_home, "홈"))
        addView(createMenuButton(R.drawable.hp_ic_dh, "하루해픽"))
        addView(FrameLayout(context).apply {
            val fab = FloatingActionButton(context).apply {
                size = FloatingActionButton.SIZE_MINI
                elevation = 0f
            }
            addView(fab, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
            })
        }, LayoutParams(0, MATCH_PARENT, 1f))
        addView(createMenuButton(R.drawable.hp_ic_hr, "해픽레포트"))
        addView(createMenuButton(R.drawable.hp_ic_set, "설정"))
    }
}