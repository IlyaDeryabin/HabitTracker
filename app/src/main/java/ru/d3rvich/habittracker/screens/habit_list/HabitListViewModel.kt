package ru.d3rvich.habittracker.screens.habit_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.base.BaseViewModel
import ru.d3rvich.habittracker.data.HabitStore
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
                updateViewState(event.filterText)
            }
        }
    }

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            setState(currentState.copy(isLoading = true))
            HabitStore.getHabits().collect { habits ->
                localHabits = habits
                updateViewState(currentState.filterConfig.filterText)
            }
        }
    }

    private fun updateViewState(searchText: String) {
        setState(HabitListViewState(
            habitList = localHabits.filter {
                it.title.contains(searchText, ignoreCase = true)
            },
            isLoading = false,
            filterConfig = currentState.filterConfig.copy(filterText = searchText)))
    }
}