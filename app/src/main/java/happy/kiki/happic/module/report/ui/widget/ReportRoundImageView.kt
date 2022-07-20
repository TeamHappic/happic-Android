package happy.kiki.happic.module.report.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.LinearLayout
import com.google.android.material.imageview.ShapeableImageView
import happy.kiki.happic.R
import happy.kiki.happic.module.core.util.loadUrlAsync
import happy.kiki.happic.module.core.util.setCornerSize

class ReportRoundImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ShapeableImageView(context, attrs) {
    init {
        scaleType = CENTER_CROP
        setCornerSize(8)
    }

    fun bind(image: String, imageSize: Int, leftMargin: Int) {
        layoutParams = LinearLayout.LayoutParams(
            imageSize, imageSize, 1f
        ).apply {
            this.leftMargin = leftMargin
        }

        if (image == "dummy") {
            setImageResource(R.drawable.bg_image_dummy)
        } else {
            loadUrlAsync(image)
        }
    }
}