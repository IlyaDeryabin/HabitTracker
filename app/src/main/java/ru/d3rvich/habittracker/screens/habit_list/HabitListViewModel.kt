package ru.d3rvich.habittracker.screens.habit_list

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.d3rvich.habittracker.R
import ru.d3rvich.habittracker.base.BaseViewModel
import ru.d3rvich.habittracker.di.ActivityScope
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.interactors.HabitInteractor
import ru.d3rvich.habittracker.domain.models.OperationStatus
import ru.d3rvich.habittracker.screens.habit_list.model.FilterConfig
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListAction
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListEvent
import ru.d3rvich.habittracker.screens.habit_list.model.HabitListViewState
import javax.inject.Inject

@ActivityScope
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
        viewModelScope.launch {
            habitsFlow.collect { habits ->
                updateViewState(currentState.filterConfig, habits)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            setState(currentState.copy(isLoading = true))
            val status = habitInteractor.updateHabits()
            if (status is OperationStatus.Failure) {
                sendAction { HabitListAction.ShowToast(R.string.check_internet_connection) }
            }
            setState(currentState.copy(isLoading = false))
        }
    }

    private fun updateViewState(
        filterConfig: FilterConfig,
        habits: List<HabitEntity>? = habitsFlow.value,
    ) {
        habits?.let {
            setState(currentState.copy(
                habitList = filterConfig.execute(habits),
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