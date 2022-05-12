package ru.d3rvich.habittracker.presenter.screens.habit_list.model

import ru.d3rvich.habittracker.presenter.base.UiState
import ru.d3rvich.habittracker.domain.entity.HabitEntity

data class HabitListViewState(
    val habitList: List<HabitEntity>?,
    val isLoading: Boolean,
    val filterConfig: FilterConfig
) : UiState
