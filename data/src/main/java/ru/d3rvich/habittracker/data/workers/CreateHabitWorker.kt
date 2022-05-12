package ru.d3rvich.habittracker.data.workers

import android.content.Context
import androidx.work.*
import com.google.gson.Gson
import ru.d3rvich.habittracker.data.dto.NewHabitDto
import ru.d3rvich.habittracker.data.mappers.toHabitDto
import ru.d3rvich.habittracker.data.remote.result.RetrofitResult
import java.util.concurrent.TimeUnit

class CreateHabitWorker(appContext: Context, params: WorkerParameters) :
    BaseHabitWorker(appContext, params) {
    override suspend fun performWork(): Result {
        val jsonHabit = inputData.getString(HABIT_KEY) ?: return Result.failure()
        val habit: NewHabitDto = Gson().fromJson(jsonHabit, NewHabitDto::class.java)
        return when (val result = apiService.createHabit(habit = habit)) {
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
            is RetrofitResult.Success -> {
                val newHabit = habit.toHabitDto(result.value.uid)
                habitDao.createHabits(newHabit)
                Result.success()
            }
        }
    }

    companion object {
        private const val HABIT_KEY = "habit"

        fun createWorker(habit: NewHabitDto): WorkRequest {
            val jsonHabit = Gson().toJson(habit)
            val inputData = workDataOf(HABIT_KEY to jsonHabit)
            return OneTimeWorkRequestBuilder<CreateHabitWorker>().apply {
                setInitialDelay(15L, TimeUnit.SECONDS)
                setInputData(inputData)
            }.build()
        }
    }
}