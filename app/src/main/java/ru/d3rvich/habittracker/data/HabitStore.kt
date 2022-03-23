package ru.d3rvich.habittracker.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.d3rvich.habittracker.entity.HabitEntity
import ru.d3rvich.habittracker.entity.HabitType
import java.util.*

object HabitStore {
    private val habits: MutableList<HabitEntity> = mutableListOf()
    private val habitsFlow = MutableStateFlow<List<HabitEntity>>(emptyList())

    init {
        runBlocking {
            launch {
                for (i in 0 until 10) {
                    val habit = HabitEntity(
                        id = UUID.randomUUID().toString(),
                        title = "Title $i",
                        description = "Description",
                        type = if (i % 2 == 0) HabitType.Good else HabitType.Bad,
                        count = 0,
                        frequency = 0,
                        priority = i % 3,
                        color = (0xFF00FF00).toInt(),
                        date = (i * 1000 + i * i).toLong(),
                        doneDates = emptyList()
                    )
                    habits.add(habit)
                }
                habitsFlow.emit(habits.toList())
            }
        }
    }

    fun getHabits(): Flow<List<HabitEntity>> {
        return habitsFlow.asSharedFlow()
    }

    suspend fun getHabitBy(id: String): HabitEntity {
        delay(1) // Оправдываю suspend
        val habit = habits.find { it.id == id }
        checkNotNull(habit) { "Habit not found" }
        return habit
    }

    suspend fun addHabit(habit: HabitEntity) {
        habits.add(habit)
        habitsFlow.emit(habits.toList())
    }

    suspend fun editHabit(habit: HabitEntity) {
        val toReplace = habits.find { it.id == habit.id }
        checkNotNull(toReplace)
        val indexToReplace = habits.indexOf(toReplace)
        check(indexToReplace > -1) { "Habit not found" }
        habits[indexToReplace] = habit
        habitsFlow.emit(habits.toList())
    }
}