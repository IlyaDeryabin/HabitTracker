package ru.d3rvich.habittracker.screens.habit_list.model

import ru.d3rvich.habittracker.base.UiEvent

sealed class HabitListEvent : UiEvent {
    object OnAddHabitButtonClicked : HabitListEvent()
    class OnHabitSelected(val id: String) : HabitListEvent()
    class OnDeleteHabit(val id: String) : HabitListEvent()
    class OnFilterChange(val filterText: String) : HabitListEvent()
    class OnSortingMethodChange(val comparator: HabitSortingEngine) : HabitListEvent()
    class OnSortDirectionChange(val direction: SortDirection) : HabitListEvent()
}
