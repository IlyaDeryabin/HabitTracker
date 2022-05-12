package ru.d3rvich.habittracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.d3rvich.habittracker.data.dto.HabitDto

@Database(version = 1, exportSchema = false, entities = [HabitDto::class])
abstract class HabitDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}

private const val DATABASE_NAME = "habit-database"

fun HabitDatabase(context: Context): HabitDatabase {
    return Room.databaseBuilder(context, HabitDatabase::class.java, DATABASE_NAME).build()
}