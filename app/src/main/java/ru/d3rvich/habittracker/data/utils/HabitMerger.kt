package ru.d3rvich.habittracker.data.utils

import ru.d3rvich.habittracker.data.dto.HabitDto

object HabitMerger {
    fun merge(remoteHabits: List<HabitDto>, localHabits: List<HabitDto>): MergeResult {
        val localIds = localHabits.map { it.id }
        val remoteIds = remoteHabits.map { it.id }
        val toRemove = localHabits.filter { it.id !in remoteIds }
        val toCreate = remoteHabits.filter { it.id !in localIds }
        val toUpdate = remoteHabits.filter { remote ->
            localHabits.any { local -> remote.id == local.id && remote != local }
        }
        return MergeResult(toRemove, toCreate, toUpdate)
    }
}

data class MergeResult(
    val listToRemove: List<HabitDto>,
    val listToCreate: List<HabitDto>,
    val listToUpdate: List<HabitDto>,
)