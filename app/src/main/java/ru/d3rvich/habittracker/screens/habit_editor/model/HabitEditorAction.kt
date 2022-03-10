package ru.d3rvich.habittracker.screens.habit_editor.model

import ru.d3rvich.habittracker.base.UiAction

sealed class HabitEditorAction : UiAction {
    object PopBackStack : HabitEditorAction()
}