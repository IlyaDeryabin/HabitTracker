package ru.d3rvich.habittracker.data.workers

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import ru.d3rvich.habittracker.data.dto.HabitDto
import ru.d3rvich.habittracker.data.remote.result.RetrofitResult
import java.util.concurrent.TimeUnit

class EditHabitWorker(appContext: Context, params: WorkerParameters) :
    BaseHabitWorker(appContext, params) {

    override suspend fun performWork(): Result {
        val jsonHabit = inputData.getString(HABIT_KEY) ?: return Result.failure()
        val habit = Gson().fromJson(jsonHabit, HabitDto::class.java)
        return when (val result = apiService.editHabit(habit)) {
            is RetrofitResult.Success -> {
                habitDao.updateHabits(habit)
                Result.success()
            }
            is RetrofitResult.Failure.Error -> {
                Result.retry()
            }
            is RetrofitResult.Failure.HttpError -> {
                when (result.statusCode) {
                    400, 401, 500 -> {
                        Result.failure()
                    }
                    else -> {
                        Result.retry()
                    }
                }
            }
        }
    }

    companion object {
        private const val HABIT_KEY = "habit"
        fun createWorker(habit: HabitDto): WorkRequest {
            val workRequest = OneTimeWorkRequestBuilder<EditHabitWorker>().apply {
                setInitialDelay(15L, TimeUnit.SECONDS)
                val inputData = workDataOf(HABIT_KEY to Gson().toJson(habit))
                setInputData(inputData)
            }.build()
            return workRequest
        }
    }
}