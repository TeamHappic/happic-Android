package happy.kiki.happic.module.core.util.extension

import android.graphics.Color
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams

class WindowHandler(private val activity: AppCompatActivity) {
    private val contentView get() = activity.findViewById<View>(android.R.id.content)
    private val insetsController: WindowInsetsControllerCompat
        get() = WindowCompat.getInsetsController(activity.window, contentView)

    fun setStatusBarTransparent() = setStatusBarColor(Color.TRANSPARENT)

    fun setStatusBarColor(@ColorInt color: Int) {
        activity.window.statusBarColor = color
    }

    fun allowViewOverlapWithStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(activity.window, false)
        ViewCompat.setOnApplyWindowInsetsListener(contentView) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updateLayoutParams<MarginLayoutParams> {
                leftMargin = insets.left
                bottomMargin = insets.bottom
                rightMargin = insets.right
            }
            WindowInsetsCompat.CONSUMED
        }
    }

    fun showStatusBar() = insetsController.show(WindowInsetsCompat.Type.statusBars())
    fun hideStatusBar() = insetsController.hide(WindowInsetsCompat.Type.statusBars())
    fun showNavigationBars() = insetsController.show(WindowInsetsCompat.Type.navigationBars())
    fun hideNavigationBars() = insetsController.hide(WindowInsetsCompat.Type.navigationBars())
}

val AppCompatActivity.windowHandler get() = WindowHandler(this)