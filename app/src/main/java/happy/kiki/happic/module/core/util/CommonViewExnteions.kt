package happy.kiki.happic.module.core.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory.Builder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import happy.kiki.happic.R
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.pxFloat
import kotlin.math.roundToInt

@BindingAdapter("android:visibility")
fun View.setVisibilityBinding(visible: Boolean) {
    isVisible = visible
}

@BindingAdapter("invisible")
fun View.setInvisibleBinding(isInvisible: Boolean) {
    this.isInvisible = isInvisible
}

@BindingAdapter("url", requireAll = false)
fun ImageView.loadUrlAsync(url: String?) {
    val anim = CircularProgressDrawable(context).apply {
        strokeWidth = 4f
        setColorSchemeColors(
            *listOf(
                R.color.white, R.color.dark_blue, R.color.orange
            ).map { context.getColor(it) }.toIntArray()
        )
        setStyle((width * 0.5).roundToInt())
        start()
    }

    if (url == null) {
        Glide.with(this).load(anim).into(this)
    } else {
        Glide.with(this).load(url)
            .transition(DrawableTransitionOptions.withCrossFade(Builder().setCrossFadeEnabled(true).build()))
            .placeholder(anim).into(this)
    }
}

@BindingAdapter("srcResource")
fun ImageView.setResourceWithId(@DrawableRes id: Int) {
    setImageResource(id)
}

@BindingAdapter("visibilityWithAnim", "duration", requireAll = false)
fun View.setVisibilityWithAnim(visible: Boolean, _duration: Long) {
    val duration = if (_duration == 0L) 200L else _duration
    if (visible) {
        animate().alpha(1f).setDuration(duration).withStartAction {
            alpha = 0f
            isVisible = true
        }.start()
    } else {
        animate().alpha(0f).setDuration(duration).withEndAction {
            isVisible = false
        }.start()
    }
}

@BindingAdapter("android:selected")
fun View.setSelectedBinding(isSelected: Boolean) {
    this.isSelected = isSelected
}

@BindingAdapter("textColorRes")
fun TextView.setTextColorWithResources(@ColorRes id: Int) {
    if (id == 0x00) return
    kotlin.runCatching {
        setTextColor(getColor(id))
    }
}

fun ShapeableImageView.setCornerSize(cornerSize: Int) {
    shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(pxFloat(cornerSize))
}

