package happy.kiki.happic.module.core.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout

class Touchable @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    init {
        isClickable = true
        isFocusable = true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        alpha = if (ev?.action == MotionEvent.ACTION_DOWN) .4f else 1f
        return super.dispatchTouchEvent(ev)
    }
}