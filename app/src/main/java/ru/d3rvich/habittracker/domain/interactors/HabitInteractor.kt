package ru.d3rvich.habittracker.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.habittracker.domain.entity.HabitEntity

interface HabitInteractor {
    fun getHabits(): Flow<List<HabitEntity>>

    suspend fun getHabitBy(id: String): HabitEntity

    suspend fun addHabit(habit: HabitEntity)

    suspend fun editHabit(habit: HabitEntity)

    suspend fun deleteHabit(habit: HabitEntity)
}