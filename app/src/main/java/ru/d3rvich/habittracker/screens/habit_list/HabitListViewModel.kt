package ru.d3rvich.habittracker.screens.habit_list

import android.os.Bundle
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import ru.d3rvich.habittracker.base.BaseViewModel
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.interactors.HabitInteractor
import ru.d3rvich.habittracker.screens.habit_list.model.FilterConfig
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListAction
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListViewState
import javax.inject.Inject

class HabitListViewModel @Inject constructor(private val habitInteractor: HabitInteractor) :
    BaseViewModel<HabitListEvent, HabitListViewState, HabitListAction>() {

    override fun createInitialState(): HabitListViewState = HabitListViewState(
        habitList = null,
        isLoading = true,
        filterConfig = FilterConfig.Empty
    )

    private val habitsFlow = habitInteractor.getHabits()
        .stateIn(CoroutineScope(context = SupervisorJob() + Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(),
            initialValue = null)

    override fun obtainEvent(event: HabitListEvent) {
        when (event) {
            HabitListEvent.OnAddHabitButtonClicked -> {
                sendAction { HabitListAction.NavigateToHabitCreator }
            }
            is HabitListEvent.OnHabitSelected -> {
                sendAction { HabitListAction.NavigateToHabitEditor(event.id) }
            }
            is HabitListEvent.OnFilterChange -> {
                updateViewState(currentState.filterConfig.copy(filterText = event.filterText))
            }
            is HabitListEvent.OnSortingMethodChange -> {
                updateViewState(currentState.filterConfig.copy(sortingEngine = event.comparator))
            }
            is HabitListEvent.OnSortDirectionChange -> {
                updateViewState(currentState.filterConfig.copy(sortDirection = event.direction))
            }
            is HabitListEvent.OnDeleteHabit -> {
                removeHabit(event.id)
            }
        }
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            setState(currentState.copy(isLoading = true))
            habitsFlow.collect { habits ->
                updateViewState(currentState.filterConfig, habits)
            }
        }
    }

    private fun updateViewState(
        filterConfig: FilterConfig,
        habits: List<HabitEntity>? = habitsFlow.value,
    ) {
        habits?.let {
            setState(HabitListViewState(
                habitList = filterConfig.execute(habits),
                isLoading = false,
                filterConfig = filterConfig))
        }
    }

    private fun removeHabit(habitId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            habitsFlow.value?.find { it.id == habitId }?.let { habitToRemove ->
                habitInteractor.deleteHabit(habitToRemove)
            }
        }
    }
}

class HabitListViewModelFactory @AssistedInject constructor(
    private val habitInteractor: HabitInteractor,
    @Assisted("owner") owner: SavedStateRegistryOwner,
    @Assisted("args") args: Bundle? = null,
) : AbstractSavedStateViewModelFactory(owner, args) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("owner") owner: SavedStateRegistryOwner,
            @Assisted("args") args: Bundle? = null,
        ): HabitListViewModelFactory
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        return HabitListViewModel(habitInteractor) as T
    }
}