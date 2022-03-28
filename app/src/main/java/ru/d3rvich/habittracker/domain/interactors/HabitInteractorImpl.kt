package ru.d3rvich.habittracker.domain.interactors

import kotlinx.coroutines.flow.Flow
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.repositories.HabitLocalRepository

class HabitInteractorImpl(private val localRepository: HabitLocalRepository) : HabitInteractor {
    override fun getHabits(): Flow<List<HabitEntity>> = localRepository.getHabits()

    override suspend fun getHabitBy(id: String): HabitEntity = localRepository.getHabitBy(id = id)

    override suspend fun addHabit(habit: HabitEntity) {
        localRepository.addHabit(habit = habit)
    }

    override suspend fun editHabit(habit: HabitEntity) {
        localRepository.editHabit(habit = habit)
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        localRepository.deleteHabit(habit = habit)
    }
}