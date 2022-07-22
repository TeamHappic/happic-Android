package happy.kiki.happic.module.dailyhappic.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import happy.kiki.happic.databinding.ItemDailyHappicDetailBinding
import happy.kiki.happic.module.dailyhappic.data.model.DailyHappicModel
import happy.kiki.happic.module.dailyhappic.ui.fragment.DailyHappicDetailAdapter.DailyHappicDetailHolder

class DailyHappicDetailAdapter : ListAdapter<DailyHappicModel, DailyHappicDetailHolder>(DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyHappicDetailHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDailyHappicDetailBinding.inflate(inflater, parent, false)
        return DailyHappicDetailHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyHappicDetailHolder, position: Int) = holder.bind(getItem(position))

    inner class DailyHappicDetailHolder(private val binding: ItemDailyHappicDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DailyHappicModel) {
            binding.image.transitionName = item.id
            binding.item = item
            binding.executePendingBindings()
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<DailyHappicModel>() {
            override fun areItemsTheSame(
                oldItem: DailyHappicModel, newItem: DailyHappicModel
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DailyHappicModel, newItem: DailyHappicModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}