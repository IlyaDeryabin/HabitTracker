package ru.d3rvich.habittracker.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.d3rvich.habittracker.entity.HabitEntity
import ru.d3rvich.habittracker.entity.HabitType
import ru.d3rvich.habittracker.screens.habit_list.views.TypedHabitListFragment

class HabitListPagerAdapter(fragment: Fragment, private val habits: List<HabitEntity>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val goodHabits = habits.filter { it.type == HabitType.Good }
                TypedHabitListFragment.newInstance(goodHabits)
            }
            else -> {
                val badHabits = habits.filter { it.type == HabitType.Bad }
                TypedHabitListFragment.newInstance(badHabits)
            }
        }
    }
}