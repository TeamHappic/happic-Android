package happy.kiki.happic.module.core.util.extension

import android.view.View
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment

fun View.getColor(@ColorRes res: Int) = context.getColor(res)
fun Fragment.getColor(@ColorRes res: Int) = requireContext().getColor(res)