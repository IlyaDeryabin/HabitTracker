package ru.d3rvich.habittracker.screens.habit_editor

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.base.BaseViewModel
import ru.d3rvich.habittracker.domain.interactors.HabitInteractor
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorAction
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorEvent
import ru.d3rvich.habittracker.screens.habit_editor.model.HabitEditorViewState

class HabitEditorViewModel(
    savedStateHandle: SavedStateHandle, private val habitInteractor: HabitInteractor,
) : BaseViewModel<HabitEditorEvent, HabitEditorViewState, HabitEditorAction>() {
    override fun createInitialState(): HabitEditorViewState {
        return HabitEditorViewState.Loading
    }

    private var habitId: String? = null

    init {
        habitId = savedStateHandle.get(HABIT_ID_KEY)
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
                loadData(habitId!!)
            }
            else -> unexpectedEventError(event, viewState)
        }
    }

    companion object {
        const val HABIT_ID_KEY = "habitId"
    }
}

class HabitEditorViewModelFactory @AssistedInject constructor(
    private val habitInteractor: HabitInteractor,
    @Assisted("owner") owner: SavedStateRegistryOwner,
    @Assisted("args") args: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, args) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("owner") owner: SavedStateRegistryOwner,
            @Assisted("args") args: Bundle? = null,
        ): HabitEditorViewModelFactory
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        return HabitEditorViewModel(handle, habitInteractor) as T
    }
}