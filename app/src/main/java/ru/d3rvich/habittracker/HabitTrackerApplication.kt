package ru.d3rvich.habittracker

import android.app.Application
import android.content.Context
import ru.d3rvich.habittracker.data.components.DataDepsStore
import ru.d3rvich.habittracker.di.AppComponent
import ru.d3rvich.habittracker.di.DaggerAppComponent

class HabitTrackerApplication : Application() {

    internal val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().applicationContext(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        DataDepsStore.deps = appComponent
    }
}

internal val Context.appComponent: AppComponent
    get() = when (this) {
        is HabitTrackerApplication -> appComponent
        else -> applicationContext.appComponent
    }