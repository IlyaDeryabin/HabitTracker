package ru.d3rvich.habittracker.screens.habit_editor.model

import androidx.annotation.StringRes
import ru.d3rvich.habittracker.base.UiState
import ru.d3rvich.habittracker.domain.entity.HabitEntity

sealed class HabitEditorViewState : UiState {
    object Loading : HabitEditorViewState()
    data class Creator(val isUploading: Boolean = false) : HabitEditorViewState()
    data class Editor(val habit: HabitEntity, val isUploading: Boolean = false) : HabitEditorViewState()
    class Error(@StringRes val messageResId: Int) : HabitEditorViewState()
}

