package ru.d3rvich.habittracker.screens.habit_editor.model

import ru.d3rvich.habittracker.base.UiEvent
import ru.d3rvich.habittracker.entity.HabitEntity

sealed class HabitEditorEvent : UiEvent {
    object OnReloadButtonPressed : HabitEditorEvent()
    class OnSaveHabitPressed(val habit: HabitEntity) : HabitEditorEvent()
}