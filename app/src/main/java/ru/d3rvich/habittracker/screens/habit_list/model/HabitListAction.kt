package ru.d3rvich.habittracker.screens.habit_list.model

import androidx.annotation.StringRes
import ru.d3rvich.habittracker.base.UiAction

sealed class HabitListAction : UiAction {
    object NavigateToHabitCreator : HabitListAction()
    class NavigateToHabitEditor(val habitId: String) : HabitListAction()
    class ShowToast(@StringRes val massageResId: Int) : HabitListAction()
}