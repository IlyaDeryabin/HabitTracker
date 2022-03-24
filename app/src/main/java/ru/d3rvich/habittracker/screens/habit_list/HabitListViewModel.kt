package ru.d3rvich.habittracker.screens.habit_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.base.BaseViewModel
import ru.d3rvich.habittracker.data.HabitRepository
import ru.d3rvich.habittracker.entity.HabitEntity
import ru.d3rvich.habittracker.screens.habit_list.model.FilterConfig
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListAction
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListViewState

class HabitListViewModel : BaseViewModel<HabitListEvent, HabitListViewState, HabitListAction>() {
    override fun createInitialState(): HabitListViewState = HabitListViewState(
        habitList = emptyList(),
        isLoading = true,
        filterConfig = FilterConfig.Empty
    )

    private val habitRepository: HabitRepository by lazy { HabitRepository.get() }
    private var localHabits: List<HabitEntity> = emptyList()

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
            habitRepository.getHabits().collect { habits ->
                localHabits = habits
                updateViewState(currentState.filterConfig)
            }
        }
    }

    private fun updateViewState(filterConfig: FilterConfig) {
        setState(HabitListViewState(
            habitList = filterConfig.execute(localHabits),
            isLoading = false,
            filterConfig = filterConfig))
    }

    private fun removeHabit(habitId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            currentState.habitList.find { it.id == habitId }?.let { habitToRemove ->
                habitRepository.deleteHabit(habitToRemove)
            }
        }
    }
}