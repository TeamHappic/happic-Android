package happy.kiki.happic.module.dailyhappic.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import happy.kiki.happic.databinding.ItemGalleryBinding
import happy.kiki.happic.module.dailyhappic.data.model.GalleryModel
import happy.kiki.happic.module.dailyhappic.ui.activity.GalleryAdapter.GalleryHolder

class GalleryAdapter : ListAdapter<GalleryModel, GalleryHolder>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGalleryBinding.inflate(inflater, parent, false)

        return GalleryHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) = holder.bind(getItem(position))

    inner class GalleryHolder(private val binding: ItemGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalleryModel) { //            binding.item = item
            //            binding.executePendingBindings()
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