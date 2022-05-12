package ru.d3rvich.habittracker.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.entity.NewHabitEntity
import ru.d3rvich.habittracker.domain.models.OperationStatus
import ru.d3rvich.habittracker.domain.repositories.HabitRepository

class HabitInteractorImpl(private val repository: HabitRepository) : HabitInteractor {
    override suspend fun updateHabits(): OperationStatus {
        return repository.updateHabits()
    }

    override fun getHabits(): Flow<List<HabitEntity>> = repository.getHabits()

    override suspend fun getHabitBy(id: String): HabitEntity = repository.getHabitBy(id = id)

    override suspend fun createHabit(newHabit: NewHabitEntity): OperationStatus {
        return repository.createHabit(habit = newHabit)
    }

    override suspend fun editHabit(habit: HabitEntity): OperationStatus {
        return repository.editHabit(habit = habit)
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        repository.deleteHabit(habit = habit)
    }
}