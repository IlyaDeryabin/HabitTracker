package ru.d3rvich.habittracker

import android.app.Application
import android.content.Context
import ru.d3rvich.habittracker.di.AppComponent
import ru.d3rvich.habittracker.di.DaggerAppComponent

class HabitTrackerApplication : Application() {

    internal val appComponent by lazy {
        DaggerAppComponent.builder().applicationContext(this).build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is HabitTrackerApplication -> appComponent
        else -> applicationContext.appComponent
    }