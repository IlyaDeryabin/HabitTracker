package ru.d3rvich.habittracker.data.workers

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import ru.d3rvich.habittracker.data.remote.result.RetrofitResult
import ru.d3rvich.habittracker.data.utils.HabitMerger
import java.util.concurrent.TimeUnit

class UpdateHabitsWorker(appContext: Context, params: WorkerParameters) :
    BaseHabitWorker(appContext, params) {
    override suspend fun performWork(): Result {
        val localHabits = habitDao.getHabitsSnapshot()
        return when (val result = apiService.getHabits()) {
            is RetrofitResult.Success -> {
                val mergeResult = HabitMerger.merge(result.value, localHabits)
                if (mergeResult.listToCreate.isNotEmpty()) {
                    habitDao.createHabits(*mergeResult.listToCreate.toTypedArray())
                }
                if (mergeResult.listToRemove.isNotEmpty()) {
                    habitDao.deleteHabits(*mergeResult.listToRemove.toTypedArray())
                }
                if (mergeResult.listToUpdate.isNotEmpty()) {
                    habitDao.updateHabits(*mergeResult.listToUpdate.toTypedArray())
                }
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
        fun create(): WorkRequest {
            return OneTimeWorkRequestBuilder<UpdateHabitsWorker>().apply {
                setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.SECONDS)
            }.build()
        }
    }
}