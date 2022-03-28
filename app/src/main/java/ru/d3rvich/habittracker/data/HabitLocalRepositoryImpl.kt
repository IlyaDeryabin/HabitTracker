package ru.d3rvich.habittracker.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.d3rvich.habittracker.data.local.HabitDao
import ru.d3rvich.habittracker.data.local.HabitDatabase
import ru.d3rvich.habittracker.data.mappers.toHabitDto
import ru.d3rvich.habittracker.data.mappers.toHabitEntity
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.repositories.HabitLocalRepository

private const val DATABASE_NAME = "habit-database"

class HabitLocalRepositoryImpl constructor(context: Context) : HabitLocalRepository {

    private val habitDatabase =
        Room.databaseBuilder(context.applicationContext, HabitDatabase::class.java, DATABASE_NAME)
            .build()

    private val habitDao: HabitDao = habitDatabase.habitDao()

    override fun getHabits(): Flow<List<HabitEntity>> {
        return habitDao.getHabits()
            .map { list -> list.map { habitDto -> habitDto.toHabitEntity() } }
    }

    override suspend fun getHabitBy(id: String): HabitEntity {
        return habitDao.getHabitBy(id).toHabitEntity()
    }

    override suspend fun addHabit(habit: HabitEntity) {
        habitDao.createHabit(habitDto = habit.toHabitDto())
    }

    override suspend fun editHabit(habit: HabitEntity) {
        habitDao.editHabit(habitDto = habit.toHabitDto())
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabit(habit.toHabitDto())
    }
}