package ru.d3rvich.habittracker.data.workers

import android.content.Context
import androidx.work.*
import ru.d3rvich.habittracker.data.dto.HabitUidDto
import ru.d3rvich.habittracker.data.remote.result.RetrofitResult
import java.util.concurrent.TimeUnit

class RemoveHabitWorker(appContext: Context, params: WorkerParameters) :
    BaseHabitWorker(appContext, params) {

    override suspend fun performWork(): Result {
        val toRemoveId = inputData.getString(HABIT_ID_KEY) ?: return Result.failure()
        return when (val result = apiService.removeHabit(habitUid = HabitUidDto(toRemoveId))) {
            is RetrofitResult.Failure.HttpError -> {
                when (result.statusCode) {
                    400 -> {
                        Result.success()
                    }
                    401, 500 -> {
                        Result.failure()
                    }
                    else -> {
                        Result.retry()
                    }
                }
            }
            is RetrofitResult.Failure.Error -> {
                Result.retry()
            }
            is RetrofitResult.Success -> {
                Result.success()
            }
        }
    }

    companion object {
        private const val HABIT_ID_KEY = "habitId"

        fun createWorker(habitId: String): WorkRequest {
            val inputData = workDataOf(HABIT_ID_KEY to habitId)
            return OneTimeWorkRequestBuilder<RemoveHabitWorker>().apply {
                setBackoffCriteria(BackoffPolicy.LINEAR, 10L, TimeUnit.SECONDS)
                setInputData(inputData)
            }.build()
        }
    }
}