package ru.d3rvich.habittracker.domain.entity

data class NewHabitEntity(
    val title: String,
    val description: String,
    val type: HabitType,
    val count: Int,
    val frequency: Int,
    val priority: Int,
    val color: Int,
    val date: Long,
    val doneDates: List<Long>,
)
