import android.R.attr
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable

fun getPressedColorSelector(normalColor: Int, pressedColor: Int) = ColorStateList(
    arrayOf(
        intArrayOf(attr.state_pressed), intArrayOf(attr.state_focused), intArrayOf(attr.state_activated), intArrayOf()
    ), intArrayOf(
        pressedColor, pressedColor, pressedColor, normalColor
    )
)

fun createMask(cornerRadius: Float) = GradientDrawable().apply {
    shape = GradientDrawable.RECTANGLE
    setColor(-0x1000000) // the color is irrelevant here, only the alpha
    this.cornerRadius = cornerRadius // you can have a rounded corner for the effect
}

fun createRippleDrawable(fillColor: Int, rippleColor: Int, cornerRadius: Float = 0f) = RippleDrawable(
    getPressedColorSelector(fillColor, rippleColor), ColorDrawable(fillColor), createMask(cornerRadius)
)