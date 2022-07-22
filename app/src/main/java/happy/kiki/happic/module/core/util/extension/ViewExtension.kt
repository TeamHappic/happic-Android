package happy.kiki.happic.module.core.util.extension

import android.content.Context
import android.os.Build.VERSION
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.View
import android.widget.EditText
import androidx.annotation.Px
import androidx.core.view.ViewCompat
import kotlin.math.roundToInt

fun View.setShadowColorIfAvailable(color: Int) {
    if (VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        outlineSpotShadowColor = color
        outlineAmbientShadowColor = color
    }
}

@Px
fun Context.px(dp: Int) = (dp * resources.displayMetrics.density).roundToInt()
fun Context.pxFloat(dp: Int) = dp * resources.displayMetrics.density

@Px
fun View.px(dp: Int) = context.px(dp)
fun View.pxFloat(dp: Int) = context.pxFloat(dp)
fun View.spPx(dp: Int) = (dp * resources.displayMetrics.scaledDensity)
val View.screenWidth: Int
    get() = resources.displayMetrics.widthPixels

val View.screenHeight: Int
    get() = resources.displayMetrics.heightPixels

fun EditText.addNoSpaceFilter(): EditText {
    filters = filters.toMutableList().apply {
        add(InputFilter { s, _, _, _, _, _ -> if (s.contains(' ')) s.trim().filter { it != ' ' } else s })
    }.toTypedArray()
    return this
}

fun EditText.addLengthFilter(length: Int): EditText {
    filters = filters.toMutableList().apply {
        add(LengthFilter(length))
    }.toTypedArray()
    return this
}

fun View.injectViewId() {
    id = ViewCompat.generateViewId()
}
