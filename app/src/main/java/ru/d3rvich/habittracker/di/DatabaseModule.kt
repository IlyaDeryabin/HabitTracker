package ru.d3rvich.habittracker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.d3rvich.habittracker.data.local.HabitDatabase
import javax.inject.Singleton

private const val DATABASE_NAME = "habit-database"

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(context: Context): HabitDatabase {
        return Room.databaseBuilder(context, HabitDatabase::class.java, DATABASE_NAME).build()
    }
}