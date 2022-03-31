package ru.d3rvich.habittracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.d3rvich.habittracker.data.dto.HabitLocalDto

@Database(entities = [HabitLocalDto::class], version = 1)
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}