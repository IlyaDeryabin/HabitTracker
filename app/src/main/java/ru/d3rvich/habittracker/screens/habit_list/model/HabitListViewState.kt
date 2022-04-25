package ru.d3rvich.habittracker.screens.habit_list.model

import ru.d3rvich.habittracker.base.UiState
import ru.d3rvich.habittracker.domain.entity.HabitEntity

data class HabitListViewState(
    val habitList: List<HabitEntity>?,
    val isLoading: Boolean,
    val filterConfig: FilterConfig
) : UiState
