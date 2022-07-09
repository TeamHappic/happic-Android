package happy.kiki.happic.module.core.util.extension

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment

class WindowHandler(private val activity: Activity) {
    private val contentView get() = activity.findViewById<View>(android.R.id.content)
    private val insetsController: WindowInsetsControllerCompat
        get() = WindowCompat.getInsetsController(activity.window, contentView)

    var statusBarColor: Int
        get() = activity.window.statusBarColor
        set(value) {
            activity.window.statusBarColor = value
        }

    fun setStatusBarTransparent() {
        statusBarColor = Color.TRANSPARENT
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
    fun showKeyboard() = insetsController.show(WindowInsetsCompat.Type.ime())
    fun hideKeyboard() = insetsController.hide(WindowInsetsCompat.Type.ime())

    var isAppearanceLightNavigationBars: Boolean
        get() = insetsController.isAppearanceLightNavigationBars
        set(value) {
            insetsController.isAppearanceLightNavigationBars = value
        }
    var isAppearanceLightStatusBars: Boolean
        get() = insetsController.isAppearanceLightStatusBars
        set(value) {
            insetsController.isAppearanceLightStatusBars = value
        }
}

val AppCompatActivity.windowHandler get() = WindowHandler(this)
val Fragment.windowHandler get() = WindowHandler(requireActivity())