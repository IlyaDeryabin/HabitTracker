package ru.d3rvich.habittracker.screens.habit_editor.model

import androidx.annotation.StringRes
import ru.d3rvich.habittracker.base.UiAction

sealed class HabitEditorAction : UiAction {
    object PopBackStack : HabitEditorAction()
    class ShowToast(@StringRes val massageResId: Int) : HabitEditorAction()
}