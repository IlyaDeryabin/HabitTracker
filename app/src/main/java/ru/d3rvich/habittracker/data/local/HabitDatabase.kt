package ru.d3rvich.habittracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.d3rvich.habittracker.data.dto.HabitDto

@Database(version = 1, exportSchema = false, entities = [HabitDto::class])
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}