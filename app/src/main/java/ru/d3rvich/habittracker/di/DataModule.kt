package ru.d3rvich.habittracker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.d3rvich.habittracker.data.HabitLocalRepositoryImpl
import ru.d3rvich.habittracker.domain.repositories.HabitLocalRepository

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideHabitLocalRepository(@ApplicationContext context: Context): HabitLocalRepository {
        return HabitLocalRepositoryImpl(context)
    }
}