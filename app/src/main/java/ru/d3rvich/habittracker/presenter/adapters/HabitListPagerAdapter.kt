package ru.d3rvich.habittracker.presenter.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.databinding.PagerItemBinding
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.entity.HabitType
import ru.d3rvich.habittracker.presenter.utils.isVisible

data class PagerItem(val targetType: HabitType, val habits: List<HabitEntity>?)

class HabitListPagerAdapter(
    private val onItemClick: (String) -> Unit,
    private val onLongClick: (String) -> Unit,
) : ListAdapter<PagerItem, HabitListPagerAdapter.ListViewHolder>(AdapterDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pager_item, parent, false)

        return ListViewHolder(view, onItemClick, onLongClick)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        require(position < 3) { "Не должно быть больше двух объектов" }
        holder.bind(getItem(position).habits)
    }

    class ListViewHolder(
        view: View,
        private val onItemClick: (String) -> Unit,
        private val onLongClick: (String) -> Unit,
    ) : RecyclerView.ViewHolder(view) {
        private val binding = PagerItemBinding.bind(view)
        private val adapter: HabitListAdapter by lazy { HabitListAdapter(onItemClick, onLongClick) }

        init {
            binding.habitList.adapter = adapter
        }

        fun bind(list: List<HabitEntity>?) {
            with(binding) {
                if (list == null) {
                    habitList.isVisible(false)
                    emptyListMessage.isVisible(false)
                } else {
                    emptyListMessage.isVisible(list.isEmpty())
                    habitList.isVisible(list.isNotEmpty())
                    adapter.submitList(list)
                }
            }
        }
    }

    class AdapterDiffUtil : DiffUtil.ItemCallback<PagerItem>() {
        override fun areItemsTheSame(oldItem: PagerItem, newItem: PagerItem): Boolean {
            return oldItem.targetType == newItem.targetType
        }

        override fun areContentsTheSame(oldItem: PagerItem, newItem: PagerItem): Boolean {
            return oldItem == newItem
        }
    }
}