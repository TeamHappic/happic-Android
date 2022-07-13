package happy.kiki.happic.module.core.util.extension

import android.content.Context
import android.os.Build.VERSION
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.annotation.Px
import kotlin.math.roundToInt

fun View.setShadowColorIfAvailable(color: Int) {
    if (VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        outlineSpotShadowColor = color
        outlineAmbientShadowColor = color
    }
}

@Px
fun Context.px(dp: Int) = (dp * resources.displayMetrics.density).roundToInt()

@Px
fun View.px(dp: Int) = context.px(dp)
fun View.pxFloat(dp: Int) = (dp * resources.displayMetrics.density)
fun View.spPx(dp: Int) = (dp * resources.displayMetrics.scaledDensity)
val View.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

fun View.getColor(@ColorRes res: Int) = context.getColor(res)

fun EditText.addNoSpaceFilter(): EditText {
    filters = filters.toMutableList().apply {
        add(InputFilter { s, _, _, _, _, _ -> s.trim().filter { it != ' ' } })
    }.toTypedArray()
    return this
}

fun EditText.addLengthFilter(length: Int): EditText {
    filters = filters.toMutableList().apply {
        add(LengthFilter(length))
    }.toTypedArray()
    return this
}
