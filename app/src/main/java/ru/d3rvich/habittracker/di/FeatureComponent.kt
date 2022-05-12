package ru.d3rvich.habittracker.di

import dagger.Subcomponent
import ru.d3rvich.habittracker.presenter.screens.habit_editor.HabitEditorFragment
import ru.d3rvich.habittracker.presenter.screens.habit_list.HabitListFragment
import ru.d3rvich.habittracker.presenter.screens.habit_list.view.FilterFragment
import javax.inject.Scope

@Subcomponent
@ActivityScope
internal interface FeatureComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureComponent
    }

    fun inject(fragment: HabitListFragment)
    fun inject(fragment: FilterFragment)
    fun inject(fragment: HabitEditorFragment)
}

@Retention(AnnotationRetention.RUNTIME)
@Scope
annotation class ActivityScope