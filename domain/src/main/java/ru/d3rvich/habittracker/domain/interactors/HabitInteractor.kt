package ru.d3rvich.habittracker.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.entity.NewHabitEntity
import ru.d3rvich.habittracker.domain.models.OperationStatus

interface HabitInteractor {
    suspend fun updateHabits(): OperationStatus

    fun getHabits(): Flow<List<HabitEntity>>

    suspend fun getHabitBy(id: String): HabitEntity

    suspend fun createHabit(newHabit: NewHabitEntity): OperationStatus

    suspend fun editHabit(habit: HabitEntity): OperationStatus

    suspend fun deleteHabit(habit: HabitEntity)
}