package ru.d3rvich.habittracker.di

import dagger.Subcomponent
import ru.d3rvich.habittracker.screens.habit_editor.HabitEditorFragment
import ru.d3rvich.habittracker.screens.habit_list.HabitListFragment
import ru.d3rvich.habittracker.screens.habit_list.view.FilterFragment

@Subcomponent
interface FeatureComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureComponent
    }

    fun inject(fragment: HabitListFragment)
    fun inject(fragment: FilterFragment)
    fun inject(fragment: HabitEditorFragment)
}