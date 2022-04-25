package ru.d3rvich.habittracker.data.repositories

import android.content.Context
import androidx.work.WorkManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.d3rvich.habittracker.data.local.HabitDao
import ru.d3rvich.habittracker.data.local.HabitDatabase
import ru.d3rvich.habittracker.data.mappers.toHabitDto
import ru.d3rvich.habittracker.data.mappers.toHabitEntity
import ru.d3rvich.habittracker.data.mappers.toNewHabitRemoteDto
import ru.d3rvich.habittracker.data.remote.HabitApiService
import ru.d3rvich.habittracker.data.remote.result.RetrofitResult
import ru.d3rvich.habittracker.data.utils.HabitMerger
import ru.d3rvich.habittracker.data.workers.CreateHabitWorker
import ru.d3rvich.habittracker.data.workers.EditHabitWorker
import ru.d3rvich.habittracker.data.workers.RemoveHabitWorker
import ru.d3rvich.habittracker.data.workers.UpdateHabitsWorker
import ru.d3rvich.habittracker.domain.entity.HabitEntity
import ru.d3rvich.habittracker.domain.entity.NewHabitEntity
import ru.d3rvich.habittracker.domain.models.OperationStatus
import ru.d3rvich.habittracker.domain.repositories.HabitRepository

class HabitRepositoryImpl constructor(
    habitDatabase: HabitDatabase,
    private val habitApiService: HabitApiService,
    private val context: Context,
) : HabitRepository {

    private val habitDao: HabitDao = habitDatabase.habitDao()

    override suspend fun updateHabits(): OperationStatus {
        val startWorker: () -> Unit = {
            val worker = UpdateHabitsWorker.create()
            WorkManager.getInstance(context).enqueue(worker)
        }
        return when (val result = habitApiService.getHabits()) {
            is RetrofitResult.Success -> {
                val localHabits = habitDao.getHabitsSnapshot()
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
                return OperationStatus.Success
            }
            is RetrofitResult.Failure.Error -> {
                startWorker()
                return OperationStatus.Failure
            }
            is RetrofitResult.Failure.HttpError -> {
                when (result.statusCode) {
                    401, 500 -> { // Не запускаем Worker
                    }
                    else -> {
                        startWorker()
                    }
                }
                OperationStatus.Failure
            }
        }
    }

    override fun getHabits(): Flow<List<HabitEntity>> {
        return habitDao.getHabits()
            .map { list -> list.map { habitDto -> habitDto.toHabitEntity() } }
    }

    override suspend fun getHabitBy(id: String): HabitEntity {
        return habitDao.getHabitBy(id).toHabitEntity()
    }

    override suspend fun createHabit(habit: NewHabitEntity): OperationStatus {
        val newHabit = habit.toNewHabitRemoteDto()
        val startWorker: () -> Unit = {
            val worker = CreateHabitWorker.createWorker(newHabit)
            WorkManager.getInstance(context).enqueue(worker)
        }
        return when (val result = habitApiService.createHabit(habit = newHabit)) {
            is RetrofitResult.Success -> {
                habitDao.createHabits(habit.toHabitDto(result.value.uid))
                OperationStatus.Success
            }
            is RetrofitResult.Failure.Error -> {
                startWorker()
                OperationStatus.Failure
            }
            is RetrofitResult.Failure.HttpError -> {
                when (result.statusCode) {
                    400, 401, 500 -> {
                    }
                    else -> {
                        startWorker()
                    }
                }
                OperationStatus.Failure
            }
        }
    }

    override suspend fun editHabit(habit: HabitEntity): OperationStatus {
        val habitDto = habit.toHabitDto()
        val startWorker: () -> Unit = {
            val worker = EditHabitWorker.createWorker(habitDto)
            WorkManager.getInstance(context).enqueue(worker)
        }
        return when (val result = habitApiService.editHabit(habit = habitDto)) {
            is RetrofitResult.Success -> {
                habitDao.updateHabits(habitDto)
                OperationStatus.Success
            }
            is RetrofitResult.Failure.Error -> {
                startWorker()
                OperationStatus.Failure
            }
            is RetrofitResult.Failure.HttpError -> {
                when (result.statusCode) {
                    400, 401, 500 -> {
                    }
                    else -> {
                        startWorker()
                    }
                }
                OperationStatus.Failure
            }
        }
    }

    override suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.deleteHabits(habit.toHabitDto())
        val worker = RemoveHabitWorker.createWorker(habitId = habit.id)
        WorkManager.getInstance(context).enqueue(worker)
    }
}