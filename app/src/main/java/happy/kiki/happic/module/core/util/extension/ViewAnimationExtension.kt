package happy.kiki.happic.module.core.util.extension

import android.animation.ObjectAnimator
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

fun View.fadeOut(delay: Long = 0L, forHide: Boolean = false) {
    ObjectAnimator.ofFloat(this, "alpha", 0f).apply {
        duration = 250
        startDelay = delay
        doOnEnd {
            if (forHide) isInvisible = true
            else isVisible = false
        }
    }.start()
}

fun View.fadeIn(delay: Long = 0L, duration: Long = 250L, dontMakeStartAlphaZero: Boolean = false) {
    if (!dontMakeStartAlphaZero) alpha = 0f
    ObjectAnimator.ofFloat(this, "alpha", 1f).apply {
        this.duration = duration
        startDelay = delay
        doOnStart {

            isInvisible = false
            isVisible = true
        }
    }.start()
}

fun View.translateYUp(delay: Long = 0L, duration: Long = 1000) {
    translationY = pxFloat(800)
    ObjectAnimator.ofFloat(this, "translationY", 0f).apply {
        this.duration = duration
        startDelay = delay
    }.start()
}