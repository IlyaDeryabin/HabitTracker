package ru.d3rvich.habittracker.presenter.screens.habit_editor.model

import ru.d3rvich.habittracker.presenter.base.UiEvent
import ru.d3rvich.habittracker.domain.entity.HabitEntity

sealed class HabitEditorEvent : UiEvent {
    object OnReloadButtonPressed : HabitEditorEvent()
    class OnSaveHabitPressed(val habit: HabitEntity) : HabitEditorEvent()
}