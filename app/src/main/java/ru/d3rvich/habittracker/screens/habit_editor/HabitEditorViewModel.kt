package ru.d3rvich.habittracker.screens.habit_editor

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.base.BaseViewModel
import ru.d3rvich.habittracker.domain.interactors.HabitInteractor
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorAction
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorEvent
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorViewState

class HabitEditorViewModel @AssistedInject constructor(
    @Assisted("habitId") private val habitId: String? = null,
    private val habitInteractor: HabitInteractor,
) : BaseViewModel<HabitEditorEvent, HabitEditorViewState, HabitEditorAction>() {
    override fun createInitialState(): HabitEditorViewState {
        return HabitEditorViewState.Loading
    }

    init {
        habitId?.let {
            loadData(it)
        } ?: setState(HabitEditorViewState.Creator())
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
            val habit = habitInteractor.getHabitBy(habitId)
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
                    habitInteractor.addHabit(event.habit)
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
                    habitInteractor.editHabit(event.habit)
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
                loadData(habitId)
            }
            else -> unexpectedEventError(event, viewState)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("habitId") habitId: String? = null): HabitEditorViewModel
    }
}