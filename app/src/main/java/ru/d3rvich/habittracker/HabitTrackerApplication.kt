package ru.d3rvich.habittracker

import android.app.Application
import ru.d3rvich.habittracker.data.HabitRepository

class HabitTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HabitRepository.initialize(this)
    }
}