package ru.d3rvich.habittracker.di

import dagger.Module
import dagger.Provides
import ru.d3rvich.habittracker.domain.interactors.HabitInteractor
import ru.d3rvich.habittracker.domain.interactors.HabitInteractorImpl
import ru.d3rvich.habittracker.domain.repositories.HabitRepository

@Module
object DomainModule {

    @Provides
    fun provideHabitInteractor(repository: HabitRepository): HabitInteractor {
        return HabitInteractorImpl(repository = repository)
    }
}