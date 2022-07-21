package happy.kiki.happic.module.dailyhappic.ui.activity

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.ImageView
import android.widget.ImageView.ScaleType.CENTER_CROP
import android.widget.ImageView.ScaleType.CENTER_INSIDE
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import happy.kiki.happic.R
import happy.kiki.happic.module.core.ui.widget.util.applyConstraint
import happy.kiki.happic.module.core.ui.widget.util.centerParent
import happy.kiki.happic.module.core.util.extension.getColor
import happy.kiki.happic.module.core.util.extension.injectViewId
import happy.kiki.happic.module.core.util.loadUrlAsync
import happy.kiki.happic.module.dailyhappic.data.model.GalleryModel
import happy.kiki.happic.module.dailyhappic.ui.activity.GalleryAdapter.GalleryHolder

class GalleryAdapter(private val onClickCamera: () -> Unit, private val onClickItem: (uri: String) -> Unit) :
    ListAdapter<GalleryModel, GalleryHolder>(DIFF) {
    override fun getItemViewType(position: Int) = when (position) {
        0 -> 0
        else -> 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val container = ConstraintLayout(parent.context).apply {
            setBackgroundColor(getColor(R.color.bg_black2))

            val iv = ImageView(context).apply {
                injectViewId()
            }
            addView(iv, ConstraintLayout.LayoutParams(MATCH_PARENT, 0).apply {
                dimensionRatio = "1:1"
            })
            applyConstraint { centerParent(iv) }
        }
        return GalleryHolder(container)
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) = holder.bind(getItem(position))

    inner class GalleryHolder(private val container: ConstraintLayout) : RecyclerView.ViewHolder(container) {
        private val imageView get() = container.getChildAt(0) as ImageView
        fun bind(item: GalleryModel) {
            if (itemViewType == 0) container.setOnClickListener { onClickCamera() }
            else container.setOnClickListener { onClickItem(item.uri) }

            if (itemViewType == 0) {
                imageView.scaleType = CENTER_INSIDE
                imageView.setImageResource(R.drawable.camera_white_20)
            } else {
                imageView.scaleType = CENTER_CROP
                imageView.loadUrlAsync(item.uri)
            }
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<GalleryModel>() {
            override fun areItemsTheSame(oldItem: GalleryModel, newItem: GalleryModel): Boolean {
                return oldItem.uri == newItem.uri

            }

            override fun areContentsTheSame(oldItem: GalleryModel, newItem: GalleryModel): Boolean {
                return oldItem == newItem
            }
        }
    }
}