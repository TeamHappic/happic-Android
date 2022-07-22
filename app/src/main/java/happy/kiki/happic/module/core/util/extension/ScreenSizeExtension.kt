package happy.kiki.happic.module.core.util.extension

import android.content.Context

val Context.screenWidth get() = resources.displayMetrics.widthPixels
val Context.screenHeight get() = resources.displayMetrics.heightPixels