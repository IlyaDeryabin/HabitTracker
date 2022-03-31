package ru.d3rvich.habittracker.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.repositories.HabitRepository

class HabitInteractorImpl(private val repository: HabitRepository) : HabitInteractor {
    override fun getHabits(): Flow<List<HabitEntity>> = repository.getHabits()

    override suspend fun getHabitBy(id: String): HabitEntity = repository.getHabitBy(id = id)

    override suspend fun addHabit(habit: HabitEntity) {
        repository.addHabit(habit = habit)
    }

    override suspend fun editHabit(habit: HabitEntity) {
        repository.editHabit(habit = habit)
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        repository.deleteHabit(habit = habit)
    }
}