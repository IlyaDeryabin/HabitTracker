package ru.d3rvich.habittracker.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.d3rvich.habittracker.appComponent
import ru.d3rvich.habittracker.data.local.HabitDao
import ru.d3rvich.habittracker.data.local.HabitDatabase
import ru.d3rvich.habittracker.data.remote.HabitApiService
import javax.inject.Inject

abstract class BaseHabitWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    @Inject
    lateinit var database: HabitDatabase

    protected val habitDao: HabitDao by lazy { database.habitDao() }

    @Inject
    lateinit var apiService: HabitApiService

    abstract suspend fun performWork(): Result

    override suspend fun doWork(): Result {
        applicationContext.appComponent.inject(this)
        return withContext(Dispatchers.IO) { performWork() }
    }
}