package ru.d3rvich.habittracker.data

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.d3rvich.habittracker.data.local.HabitDao
import ru.d3rvich.habittracker.data.local.HabitDatabase
import ru.d3rvich.habittracker.data.mappers.toHabitDto
import ru.d3rvich.habittracker.data.mappers.toHabitEntity
import ru.d3rvich.habittracker.entity.HabitEntity

private const val DATABASE_NAME = "habit-database"

class HabitRepository private constructor(context: Context) {

    private val habitDatabase =
        Room.databaseBuilder(context.applicationContext, HabitDatabase::class.java, DATABASE_NAME)
            .build()

    private val habitDao: HabitDao = habitDatabase.habitDao()

    fun getHabits(): Flow<List<HabitEntity>> {
        return habitDao.getHabits()
            .map { list -> list.map { habitDto -> habitDto.toHabitEntity() } }
    }

    suspend fun getHabitBy(id: String): HabitEntity {
        return habitDao.getHabitBy(id).toHabitEntity()
    }

    suspend fun addHabit(habit: HabitEntity) {
        habitDao.createHabit(habitDto = habit.toHabitDto())
    }

    suspend fun editHabit(habit: HabitEntity) {
        habitDao.editHabit(habitDto = habit.toHabitDto())
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabit(habit.toHabitDto())
    }

    companion object {
        private var INSTANCE: HabitRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = HabitRepository(context)
            }
        }

        fun get(): HabitRepository {
            return INSTANCE ?: error("Репозиторий должен быть создан")
        }
    }
}