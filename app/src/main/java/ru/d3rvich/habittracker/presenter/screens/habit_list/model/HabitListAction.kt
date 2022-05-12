package ru.d3rvich.habittracker.presenter.screens.habit_list.model

import androidx.annotation.StringRes
import ru.d3rvich.habittracker.presenter.base.UiAction

sealed class HabitListAction : UiAction {
    object NavigateToHabitCreator : HabitListAction()
    class NavigateToHabitEditor(val habitId: String) : HabitListAction()
    class ShowToast(@StringRes val massageResId: Int) : HabitListAction()
}