package happy.kiki.happic.module.core.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imgUrl")
fun ImageView.loadImg(url: String) {
    Glide.with(this).load(url).into(this)
}