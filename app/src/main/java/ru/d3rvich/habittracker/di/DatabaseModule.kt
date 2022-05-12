package ru.d3rvich.habittracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.d3rvich.habittracker.data.local.HabitDatabase
import javax.inject.Singleton

@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideHabitDatabase(context: Context): HabitDatabase {
        return HabitDatabase(context = context)
    }
}