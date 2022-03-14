package ru.d3rvich.habittracker.screens.habit_editor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.base.BaseViewModel
import ru.d3rvich.habittracker.data.HabitStore
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorAction
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorEvent
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorViewState

class HabitEditorViewModel(savedStateHandle: SavedStateHandle) :
    BaseViewModel<HabitEditorEvent, HabitEditorViewState, HabitEditorAction>() {
    override fun createInitialState(): HabitEditorViewState {
        return HabitEditorViewState.Loading
    }

    private var habitId: String? = null

    init {
        habitId = savedStateHandle.get(HABIT_ID_KEY)
        if (habitId == null) {
            setState(HabitEditorViewState.Creator())
        } else {
            loadData(habitId!!)
        }
    }

    override fun obtainEvent(event: HabitEditorEvent) {
        when (val state = currentState) {
            is HabitEditorViewState.Loading -> reduce(event, state)
            is HabitEditorViewState.Creator -> reduce(event, state)
            is HabitEditorViewState.Editor -> reduce(event, state)
            is HabitEditorViewState.Error -> reduce(event, state)
        }
    }

    private fun loadData(habitId: String) {
        viewModelScope.launch {
            val habit = HabitStore.getHabitBy(habitId)
            setState(HabitEditorViewState.Editor(habit))
        }
    }

    private fun reduce(event: HabitEditorEvent, viewState: HabitEditorViewState.Loading) {
        unexpectedEventError(event, viewState)
    }

    private fun reduce(event: HabitEditorEvent, viewState: HabitEditorViewState.Creator) {
        when (event) {
            is HabitEditorEvent.OnSaveHabitPressed -> {
                viewModelScope.launch {
                    setState(viewState.copy(isUploading = true))
                    HabitStore.addHabit(event.habit)
                    sendAction { HabitEditorAction.PopBackStack }
                }
            }
            else -> unexpectedEventError(event, viewState)
        }
    }

    private fun reduce(event: HabitEditorEvent, viewState: HabitEditorViewState.Editor) {
        when (event) {
            is HabitEditorEvent.OnSaveHabitPressed -> {
                viewModelScope.launch {
                    setState(viewState.copy(habit = event.habit, isUploading = true))
                    HabitStore.editHabit(event.habit)
                    sendAction { HabitEditorAction.PopBackStack }
                }
            }
            else -> unexpectedEventError(event, viewState)
        }
    }

    private fun reduce(event: HabitEditorEvent, viewState: HabitEditorViewState.Error) {
        when (event) {
            HabitEditorEvent.OnReloadButtonPressed -> {
                requireNotNull(habitId)
                loadData(habitId!!)
            }
            else -> unexpectedEventError(event, viewState)
        }
    }

    companion object {
        const val HABIT_ID_KEY = "habitId"
    }
}