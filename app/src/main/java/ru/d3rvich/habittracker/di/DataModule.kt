package ru.d3rvich.habittracker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.d3rvich.habittracker.data.HabitLocalRepositoryImpl
import ru.d3rvich.habittracker.data.local.HabitDatabase
import ru.d3rvich.habittracker.domain.repositories.HabitLocalRepository

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideHabitLocalRepository(habitDatabase: HabitDatabase): HabitLocalRepository {
        return HabitLocalRepositoryImpl(habitDatabase = habitDatabase)
    }
}