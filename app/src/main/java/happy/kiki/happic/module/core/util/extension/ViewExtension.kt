package happy.kiki.happic.module.core.util.extension

import android.os.Build.VERSION
import android.view.View
import androidx.annotation.Px
import kotlin.math.roundToInt

fun View.setShadowColorIfAvailable(color: Int) {
    if (VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
        outlineSpotShadowColor = color
        outlineAmbientShadowColor = color
    }
}

@Px
fun View.px(dp: Int) = (dp * resources.displayMetrics.density).roundToInt()
fun View.pxFloat(dp: Int) = (dp * resources.displayMetrics.density)
fun View.spPx(dp: Int) = (dp * resources.displayMetrics.scaledDensity)
val View.screenHeight: Int
    get() = resources.displayMetrics.heightPixels
