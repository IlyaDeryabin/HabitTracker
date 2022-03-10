package ru.d3rvich.habittracker.screens.habit_list.model

import ru.d3rvich.habittracker.base.UiEvent

sealed class HabitListEvent : UiEvent {
    object OnAddHabitButtonClicked : HabitListEvent()
    class OnHabitSelected(val id: String) : HabitListEvent()
}
