package ru.d3rvich.habittracker.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.databinding.HabitListItemBinding
import ru.d3rvich.habittracker.entity.HabitEntity
import ru.d3rvich.habittracker.entity.HabitType

class HabitListAdapter(
    private val onItemClick: (String) -> Unit,
    private val onLongClick: (String) -> Unit,
) : ListAdapter<HabitEntity, HabitListAdapter.HabitListViewHolder>(HabitListItemCallback()) {

    class HabitListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = HabitListItemBinding.bind(view)

        fun bind(habit: HabitEntity, onItemClick: (String) -> Unit, onLongClick: (String) -> Unit) {
            with(binding) {
                habitTitle.text = habit.title
                habitDescription.text = habit.description
                habitPriority.text = habit.priority.toString()
                habitColor.setBackgroundColor(habit.color)
                habitType.apply {
                    val typeText = view.context.getString(
                        when (habit.type) {
                            HabitType.Good -> R.string.good
                            HabitType.Bad -> R.string.bad
                        }
                    )
                    text = typeText
                    val textColor: Int = when (habit.type) {
                        HabitType.Good -> {
                            Color.GREEN
                        }
                        HabitType.Bad -> {
                            Color.RED
                        }
                    }
                    setTextColor(textColor)
                }
                habitCount.text = view.context.getString(R.string.times_holder, habit.count)

                habitItem.setOnClickListener {
                    onItemClick(habit.id)
                }
                habitItem.setOnLongClickListener {
                    onLongClick(habit.id)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.habit_list_item, parent, false)

        return HabitListViewHolder(view)
    }

    override fun onBindViewHolder(holder: HabitListViewHolder, position: Int) {
        val habit = getItem(position)
        holder.bind(habit, onItemClick, onLongClick)
    }

    class HabitListItemCallback : DiffUtil.ItemCallback<HabitEntity>() {
        override fun areItemsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: HabitEntity, newItem: HabitEntity): Boolean {
            return oldItem == newItem
        }
    }
}