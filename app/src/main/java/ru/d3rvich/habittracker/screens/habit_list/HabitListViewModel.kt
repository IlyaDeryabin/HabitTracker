package ru.d3rvich.habittracker.screens.habit_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.base.BaseViewModel
import ru.d3rvich.habittracker.data.HabitStore
import ru.d3rvich.habittracker.entity.HabitEntity
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListAction
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListViewState

class HabitListViewModel : BaseViewModel<HabitListEvent, HabitListViewState, HabitListAction>() {
    private var _habits: List<HabitEntity> = emptyList()
    override fun createInitialState(): HabitListViewState = HabitListViewState(
        habitList = emptyList(),
        isLoading = true
    )

    override fun obtainEvent(event: HabitListEvent) {
        when (event) {
            HabitListEvent.OnAddHabitButtonClicked -> {
                sendAction { HabitListAction.NavigateToHabitCreator }
            }
            is HabitListEvent.OnHabitSelected -> {
                sendAction { HabitListAction.NavigateToHabitEditor(event.id) }
            }
            is HabitListEvent.OnFilterChange -> {
                filterBy(event.filterText)
            }
        }
    }

    init {
        loadData()
    }

    private fun filterBy(title: String) {
        viewModelScope.launch {
            setState(currentState.copy(isLoading = true))
            setState(HabitListViewState(
                habitList = _habits.filter {
                    it.title.contains(title, ignoreCase = true)
                }, isLoading = false))
        }
    }

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            setState(currentState.copy(isLoading = true))
            HabitStore.getHabits().collect { habits ->
                _habits = habits
                setState(HabitListViewState(habits, isLoading = false))
            }
        }
    }
}