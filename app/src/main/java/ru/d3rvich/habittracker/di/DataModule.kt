package ru.d3rvich.habittracker.di

import dagger.Module
import dagger.Provides
import ru.d3rvich.habittracker.data.repositories.HabitRepositoryImpl
import ru.d3rvich.habittracker.data.local.HabitDatabase
import ru.d3rvich.habittracker.data.remote.HabitApiService
import ru.d3rvich.habittracker.domain.repositories.HabitRepository

@Module(includes = [DatabaseModule::class, NetworkModule::class])
object DataModule {

    @Provides
    fun provideHabitLocalRepositoryImpl(
        habitDatabase: HabitDatabase,
        habitApiService: HabitApiService,
    ): HabitRepository {
        return HabitRepositoryImpl(habitDatabase = habitDatabase, habitApiService = habitApiService)
    }
}