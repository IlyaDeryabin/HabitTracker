package ru.d3rvich.habittracker.di

import dagger.Module
import dagger.Provides
import ru.d3rvich.habittracker.BuildConfig
import ru.d3rvich.habittracker.data.remote.HabitApiService
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideHabitApiService(): HabitApiService {
        return HabitApiService(apiKey = BuildConfig.API_KEY)
    }
}