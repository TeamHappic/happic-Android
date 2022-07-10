import android.R.attr
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.RippleDrawable

fun getPressedColorSelector(normalColor: Int, pressedColor: Int) = ColorStateList(
    arrayOf(
        intArrayOf(attr.state_pressed), intArrayOf(attr.state_focused), intArrayOf(attr.state_activated), intArrayOf()
    ), intArrayOf(
        pressedColor, pressedColor, pressedColor, normalColor
    )
)

fun createRippleDrawable(fillColor: Int, rippleColor: Int) = RippleDrawable(
    getPressedColorSelector(fillColor, rippleColor), ColorDrawable(fillColor), null
)