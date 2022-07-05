package happy.kiki.happic.module.core.util

import android.util.Log
import happy.kiki.happic.BuildConfig

fun debugE(vararg something: Any) {
    if (!BuildConfig.DEBUG) return

    var result = "["

    something.forEachIndexed { index, any ->
        if (index > 0) result += ", "
        result += any.toString()
    }

    result += "]"

    Log.e("debug", result)
}